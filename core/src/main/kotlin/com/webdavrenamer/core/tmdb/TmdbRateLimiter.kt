package com.webdavrenamer.core.tmdb

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit

/**
 * TMDB 限流与重试拦截器（计划 §5.4 / Task 2.2.3，红线：尊重 40 req/10s + 429 退避）。
 */

/**
 * 令牌桶式（滑动窗口）限流拦截器：维持最近 [windowMillis] 内的时间戳队列，
 * 超过 [maxRequests] 时 sleep 到最早时间戳滑出窗口。
 *
 * OkHttp interceptor 的 intercept 是同步的；调用方一般在 IO 线程，[Thread.sleep] 可接受。
 */
class TmdbRateLimitInterceptor(
    private val maxRequests: Int = DEFAULT_MAX_REQUESTS,
    private val windowMillis: Long = DEFAULT_WINDOW_MILLIS,
    private val sleeper: Sleeper = RealSleeper,
) : Interceptor {

    private val timestamps = ConcurrentLinkedDeque<Long>()

    override fun intercept(chain: Interceptor.Chain): Response {
        acquireSlot()
        return try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            // 网络异常不应占用后续配额——但时间戳已记录，简化处理：保留以便统计
            throw e
        }
    }

    private fun acquireSlot() {
        while (true) {
            val now = System.currentTimeMillis()
            val cutoff = now - windowMillis
            // 清理过期时间戳
            while (true) {
                val oldest = timestamps.peekFirst() ?: break
                if (oldest < cutoff) {
                    timestamps.pollFirst()
                } else {
                    break
                }
            }
            if (timestamps.size < maxRequests) {
                timestamps.addLast(now)
                return
            }
            // 计算需要等待的时间
            val oldest = timestamps.peekFirst() ?: return
            val sleepMillis = oldest + windowMillis - now
            if (sleepMillis > 0) {
                sleeper.sleep(sleepMillis)
            }
        }
    }

    /** 测试用：当前窗口内已记录的请求数。 */
    internal fun recordedCount(): Int = timestamps.size

    companion object {
        const val DEFAULT_MAX_REQUESTS = 40
        const val DEFAULT_WINDOW_MILLIS = 10_000L
    }
}

/**
 * 429 退避重试拦截器：响应 429 时读取 `Retry-After` 头（秒）后 sleep 重试；
 * 无该头则指数退避（1s, 2s, 4s）。最多重试 [maxRetries] 次。
 *
 * 也会处理 503（Service Unavailable）的 Retry-After。
 */
class TmdbRetryInterceptor(
    private val maxRetries: Int = 3,
    private val sleeper: Sleeper = RealSleeper,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var lastResponse: Response? = null
        while (true) {
            lastResponse?.close()
            val response = chain.proceed(chain.request())
            if (response.code != 429 && response.code != 503) {
                return response
            }
            if (attempt >= maxRetries) {
                return response
            }
            val retryAfterSeconds = parseRetryAfter(response)
            val sleepMillis = if (retryAfterSeconds != null) {
                TimeUnit.SECONDS.toMillis(retryAfterSeconds)
            } else {
                exponentialBackoffMillis(attempt)
            }
            response.close()
            sleeper.sleep(sleepMillis)
            attempt++
        }
    }

    private fun parseRetryAfter(response: Response): Long? {
        val header = response.header("Retry-After") ?: return null
        // Retry-After 可能是 delta-seconds 或 HTTP-date；只处理秒。
        return header.trim().toLongOrNull()
    }

    private fun exponentialBackoffMillis(attempt: Int): Long {
        // attempt 0 -> 1s, 1 -> 2s, 2 -> 4s, ...
        return TimeUnit.SECONDS.toMillis(1L shl attempt)
    }
}

/**
 * 注入 API key 与 Accept 头的拦截器（计划 §5.4）。
 *
 * 红线：所有请求带 `Accept: application/json`。
 */
class TmdbApiKeyInterceptor(
    private val apiKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val request = original.newBuilder()
            .url(url)
            .header("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}

/** 可注入的 sleep 抽象（测试可替换为 fake）。 */
fun interface Sleeper {
    fun sleep(millis: Long)
}

object RealSleeper : Sleeper {
    override fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}
