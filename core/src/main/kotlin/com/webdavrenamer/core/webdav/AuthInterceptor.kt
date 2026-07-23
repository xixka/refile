package com.webdavrenamer.core.webdav

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Basic/Digest Auth 自动协商拦截器（计划 §M1 SubTask 1.2.3）。
 *
 * 协商流程（符合 HTTP spec「先无凭据发请求，按 401 的 WWW-Authenticate 头响应」）：
 * 1. 首请求不带 `Authorization` 头直接发出。
 * 2. 若返回 401 且有 `WWW-Authenticate` 头：解析 challenge，判断 Basic/Digest。
 *    - Basic：用 [Credentials.basic] 生成 `Authorization: Basic xxx`，重发原请求。
 *    - Digest：按 RFC 2617（qop=auth）计算响应，生成 `Authorization: Digest xxx`，重发原请求。
 * 3. 防止无限循环：用 request tag 标记已认证，最多重试 1 次。
 *
 * 红线：密码禁止进入日志；本拦截器不做任何日志输出。密码仅用于即时计算凭据后丢弃。
 *
 * @param username 用户名（可空，为空则不协商）。
 * @param password 密码（可空）。
 */
class AuthInterceptor(
    private val username: String?,
    private val password: String?,
) : Interceptor {

    /** 用于标记已尝试过一次认证，避免无限重试循环。 */
    private object AuthAttempted

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val response = chain.proceed(original)
        if (response.code != 401) return response
        // 已尝试过一次认证仍 401，不再重试。
        if (original.tag(AuthAttempted::class.java) != null) return response
        // 缺凭据无法协商。
        if (username.isNullOrEmpty() || password == null) return response
        val authHeader = response.header("WWW-Authenticate") ?: return response
        val parsed = parseChallenge(authHeader) ?: return response
        val (scheme, params) = parsed
        val authValue: String = when (scheme.lowercase()) {
            "basic" -> Credentials.basic(username, password)
            "digest" -> buildDigestAuth(original, params) ?: return response
            else -> return response
        }
        response.close()
        val retryRequest = original.newBuilder()
            .header("Authorization", authValue)
            .tag(AuthAttempted::class.java, AuthAttempted)
            .build()
        return chain.proceed(retryRequest)
    }

    /**
     * 解析 WWW-Authenticate 头：返回 (scheme, params)。
     * 例：`Digest realm="r", qop="auth", nonce="n", opaque="o"` → ("Digest", {realm=r, qop=auth, ...})。
     */
    private fun parseChallenge(header: String): Pair<String, Map<String, String>>? {
        val space = header.indexOf(' ')
        if (space <= 0) return null
        val scheme = header.substring(0, space).trim()
        if (scheme.isEmpty()) return null
        val params = parseParams(header.substring(space + 1))
        return scheme to params
    }

    /** 解析 `key=value` 逗号分隔的参数列表，支持引号字符串与转义。 */
    private fun parseParams(s: String): Map<String, String> {
        val result = LinkedHashMap<String, String>()
        var i = 0
        val n = s.length
        while (i < n) {
            while (i < n && (s[i].isWhitespace() || s[i] == ',')) i++
            if (i >= n) break
            val keyStart = i
            while (i < n && s[i] != '=' && s[i] != ',') i++
            if (i >= n || s[i] != '=') break
            val key = s.substring(keyStart, i).trim().lowercase()
            i++ // skip '='
            while (i < n && s[i].isWhitespace()) i++
            if (i >= n) {
                result[key] = ""
                break
            }
            val value: String = if (s[i] == '"') {
                i++ // skip opening quote
                val sb = StringBuilder()
                while (i < n && s[i] != '"') {
                    if (s[i] == '\\' && i + 1 < n) {
                        sb.append(s[i + 1]); i += 2
                    } else {
                        sb.append(s[i]); i++
                    }
                }
                if (i < n) i++ // skip closing quote
                sb.toString()
            } else {
                val valStart = i
                while (i < n && s[i] != ',') i++
                s.substring(valStart, i).trim()
            }
            result[key] = value
        }
        return result
    }

    /**
     * 构建 Digest `Authorization` 头值（RFC 2617，qop=auth）。
     * HA1 = MD5(user:realm:pass)，HA2 = MD5(method:uri)，
     * response = MD5(HA1:nonce:nc:cnonce:qop:HA2)。
     */
    private fun buildDigestAuth(request: Request, params: Map<String, String>): String? {
        val realm = params["realm"] ?: ""
        val nonce = params["nonce"] ?: return null
        val qop = params["qop"]
        val opaque = params["opaque"]
        val algorithm = params["algorithm"]
        val method = request.method
        val uri = request.url.encodedPath +
            (request.url.encodedQuery?.let { "?$it" } ?: "")
        val ha1 = md5Hex("$username:$realm:$password")
        val ha2 = md5Hex("$method:$uri")
        val useQopAuth = qop != null && qop.split(',').any { it.trim() == "auth" }
        val sb = StringBuilder("Digest ")
        sb.append("username=\"").append(username).append('"')
        sb.append(", realm=\"").append(realm).append('"')
        sb.append(", nonce=\"").append(nonce).append('"')
        sb.append(", uri=\"").append(uri).append('"')
        val response: String
        if (useQopAuth) {
            val nc = "00000001"
            val cnonce = randomCnonce()
            response = md5Hex("$ha1:$nonce:$nc:$cnonce:auth:$ha2")
            sb.append(", qop=auth")
            sb.append(", nc=").append(nc)
            sb.append(", cnonce=\"").append(cnonce).append('"')
        } else {
            // RFC 2069 兼容（无 qop）。
            response = md5Hex("$ha1:$nonce:$ha2")
        }
        sb.append(", response=\"").append(response).append('"')
        if (algorithm != null) {
            sb.append(", algorithm=").append(algorithm)
        }
        if (opaque != null) {
            sb.append(", opaque=\"").append(opaque).append('"')
        }
        return sb.toString()
    }

    private fun md5Hex(s: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(s.toByteArray(Charsets.UTF_8))
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val v = b.toInt() and 0xFF
            sb.append(HEX[v ushr 4])
            sb.append(HEX[v and 0x0F])
        }
        return sb.toString()
    }

    private fun randomCnonce(): String {
        val bytes = ByteArray(8)
        SecureRandom().nextBytes(bytes)
        val sb = StringBuilder(16)
        for (b in bytes) {
            val v = b.toInt() and 0xFF
            sb.append(HEX[v ushr 4])
            sb.append(HEX[v and 0x0F])
        }
        return sb.toString()
    }

    private companion object {
        private val HEX = "0123456789abcdef".toCharArray()
    }
}
