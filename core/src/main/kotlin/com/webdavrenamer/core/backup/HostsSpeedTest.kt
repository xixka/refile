package com.webdavrenamer.core.backup

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress
import java.util.concurrent.TimeUnit

/**
 * Hosts 自动测速（spec §5.3.3）。
 *
 * 对每个候选 IP 构造一条 HTTPS 请求（自定义 DNS 把目标 hostname 解析到该 IP，
 * TLS SNI/证书校验仍由 OkHttp 基于请求 URL 的 hostname 处理），测：
 * - 延迟：请求开始到响应返回的耗时（ms）。
 * - 可用性：HTTP 状态码 2xx/3xx 算可用。
 * - 失败：超时/IO 异常返回 [errorMessage]，[latencyMs]/[statusCode] 为 null。
 *
 * [testAllIps] 并行测速，[pickFastest] 选延迟最低且可用的。
 *
 * @param baseClient 测速用基础 client，本类在其上 newBuilder() 改 DNS+超时；
 *                   测试可注入信任自签证书的 client 以跑 HTTPS MockWebServer。
 */
class HostsSpeedTest(
    private val baseClient: OkHttpClient = OkHttpClient(),
) {

    /**
     * 单 IP 测速结果。
     *
     * @param ip 被测 IP。
     * @param latencyMs 延迟（毫秒），失败时为 null。
     * @param isAvailable 是否可用（HTTP 状态码 2xx/3xx）。
     * @param statusCode HTTP 状态码，失败时为 null。
     * @param errorMessage 失败原因（异常 message），成功时为 null。
     */
    data class IpSpeedTestResult(
        val ip: String,
        val latencyMs: Long?,
        val isAvailable: Boolean,
        val statusCode: Int?,
        val errorMessage: String?,
    )

    /**
     * 测试单个 IP 的连通性 + 延迟。
     *
     * 构造 [Dns]：对目标 [hostname] 返回 [ip]，其它回退系统 DNS。
     * 用 HTTPS 请求 `https://{hostname}:{port}{path}`，HEAD 方法（轻量）。
     * 超时 5s。
     *
     * @param hostname 目标域名（用于 SNI/Host/证书校验）。
     * @param ip 被测 IP 字面量。
     * @param port 端口，默认 443。
     * @param path 请求路径，默认 `/`。
     */
    suspend fun testIp(
        hostname: String,
        ip: String,
        port: Int = 443,
        path: String = "/",
    ): IpSpeedTestResult = withContext(Dispatchers.IO) {
        // 自定义 DNS：目标 hostname → 该 IP，其它走系统。
        val pinDns = object : Dns {
            override fun lookup(name: String): List<InetAddress> =
                if (name.equals(hostname, ignoreCase = true)) {
                    listOf(InetAddress.getByName(ip))
                } else {
                    Dns.SYSTEM.lookup(name)
                }
        }

        val client = baseClient.newBuilder()
            .dns(pinDns)
            .connectTimeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .callTimeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .build()

        val portPart = if (port == 443) "" else ":$port"
        val request = Request.Builder()
            .url("https://$hostname$portPart$path")
            .head()
            .build()

        val startNanos = System.nanoTime()
        try {
            client.newCall(request).execute().use { response ->
                val latency = (System.nanoTime() - startNanos) / NANOS_PER_MS
                val code = response.code
                val available = code in 200..399
                IpSpeedTestResult(
                    ip = ip,
                    latencyMs = latency,
                    isAvailable = available,
                    statusCode = code,
                    errorMessage = if (available) null else "HTTP $code",
                )
            }
        } catch (e: Exception) {
            val latency = (System.nanoTime() - startNanos) / NANOS_PER_MS
            IpSpeedTestResult(
                ip = ip,
                latencyMs = null,
                isAvailable = false,
                statusCode = null,
                errorMessage = e.message ?: e::class.simpleName ?: "未知错误",
            )
        }
    }

    /**
     * 并行测速一组 IP。
     *
     * 用 [coroutineScope] + [async] 并发，[awaitAll] 等所有完成。结果顺序与 [ips] 一致。
     */
    suspend fun testAllIps(hostname: String, ips: List<String>): List<IpSpeedTestResult> =
        coroutineScope {
            ips.map { ip ->
                async { testIp(hostname, ip) }
            }.awaitAll()
        }

    /**
     * 选出延迟最低且可用的 IP。全部失败返回 null。
     *
     * 调用 [testAllIps] 后过滤可用结果按延迟升序取首个。
     */
    suspend fun pickFastest(hostname: String, ips: List<String>): IpSpeedTestResult? {
        val results = testAllIps(hostname, ips)
        return results
            .filter { it.isAvailable && it.latencyMs != null }
            .minByOrNull { it.latencyMs!! }
    }

    private companion object {
        const val TIMEOUT_MS = 5_000L
        const val NANOS_PER_MS = 1_000_000L
    }
}
