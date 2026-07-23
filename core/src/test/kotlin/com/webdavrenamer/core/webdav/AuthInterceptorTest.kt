package com.webdavrenamer.core.webdav

import com.google.common.truth.Truth.assertThat
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest

class AuthInterceptorTest {

    private lateinit var server: MockWebServer

    @Before fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After fun tearDown() {
        server.shutdown()
    }

    private fun client(user: String?, pass: String?): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(AuthInterceptor(user, pass)).build()

    private fun request(): Request =
        Request.Builder().url(server.url("/")).get().build()

    @Test fun `first request carries no Authorization header`() {
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", "Basic realm=\"r\""))
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))

        val response = client("user", "pass").newCall(request()).execute()
        response.use {
            assertThat(it.code).isEqualTo(200)
            assertThat(it.body?.string()).isEqualTo("ok")
        }

        val firstReq = server.takeRequest()
        assertThat(firstReq.getHeader("Authorization")).isNull()
    }

    @Test fun `basic negotiation resends with Basic Authorization header`() {
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", "Basic realm=\"r\""))
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))

        val response = client("user", "pass").newCall(request()).execute()
        response.use {
            assertThat(it.code).isEqualTo(200)
        }

        // 第二个请求应携带 Basic 头
        server.takeRequest() // 首次无凭据
        val retry = server.takeRequest()
        val expected = Credentials.basic("user", "pass")
        assertThat(retry.getHeader("Authorization")).isEqualTo(expected)
    }

    @Test fun `digest negotiation resends with Digest Authorization header`() {
        val challenge = "Digest realm=\"testrealm@host\", qop=\"auth\", nonce=\"abc123nonce\", opaque=\"op42\""
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", challenge))
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))

        val response = client("user", "pass").newCall(request()).execute()
        response.use { assertThat(it.code).isEqualTo(200) }

        server.takeRequest() // 首次无凭据
        val retry = server.takeRequest()
        val authHeader = retry.getHeader("Authorization")
        assertThat(authHeader).isNotNull()
        assertThat(authHeader).startsWith("Digest ")

        val params = parseDigestParams(authHeader!!.substring("Digest ".length))
        assertThat(params["username"]).isEqualTo("user")
        assertThat(params["realm"]).isEqualTo("testrealm@host")
        assertThat(params["nonce"]).isEqualTo("abc123nonce")
        assertThat(params["uri"]).isEqualTo("/")
        assertThat(params["qop"]).isEqualTo("auth")
        assertThat(params["nc"]).isEqualTo("00000001")
        assertThat(params["opaque"]).isEqualTo("op42")
        assertThat(params["cnonce"]).isNotNull()
        assertThat(params["response"]).isNotNull()

        // 重新计算 response 验证 RFC 2617 正确性
        val ha1 = md5Hex("user:testrealm@host:pass")
        val ha2 = md5Hex("GET:/")
        val expected = md5Hex("$ha1:abc123nonce:00000001:${params["cnonce"]}:auth:$ha2")
        assertThat(params["response"]).isEqualTo(expected)
    }

    @Test fun `digest without qop uses RFC 2069 fallback`() {
        val challenge = "Digest realm=\"r\", nonce=\"n1\""
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", challenge))
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))

        client("user", "pass").newCall(request()).execute().close()

        server.takeRequest()
        val retry = server.takeRequest()
        val authHeader = retry.getHeader("Authorization")!!
        assertThat(authHeader).startsWith("Digest ")
        val params = parseDigestParams(authHeader.substring("Digest ".length))
        // 无 qop 时不应出现 qop/nc/cnonce
        assertThat(params.containsKey("qop")).isFalse()
        val ha1 = md5Hex("user:r:pass")
        val ha2 = md5Hex("GET:/")
        val expected = md5Hex("$ha1:n1:$ha2")
        assertThat(params["response"]).isEqualTo(expected)
    }

    @Test fun `loop prevention retries at most once`() {
        // 两次 401：第一次触发重试，第二次直接返回（不再重试）
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", "Basic realm=\"r\""))
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", "Basic realm=\"r\""))

        val response = client("user", "pass").newCall(request()).execute()
        response.use { assertThat(it.code).isEqualTo(401) }

        assertThat(server.requestCount).isEqualTo(2)
    }

    @Test fun `no credentials does not retry`() {
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", "Basic realm=\"r\""))
        val response = client(null, null).newCall(request()).execute()
        response.use { assertThat(it.code).isEqualTo(401) }
        assertThat(server.requestCount).isEqualTo(1)
    }

    @Test fun `non 401 response returned as-is without retry`() {
        server.enqueue(MockResponse().setResponseCode(403).setBody("forbidden"))
        val response = client("user", "pass").newCall(request()).execute()
        response.use {
            assertThat(it.code).isEqualTo(403)
            assertThat(it.body?.string()).isEqualTo("forbidden")
        }
        assertThat(server.requestCount).isEqualTo(1)
    }

    @Test fun `unknown auth scheme does not retry`() {
        server.enqueue(MockResponse().setResponseCode(401).setHeader("WWW-Authenticate", "Bearer realm=\"r\""))
        val response = client("user", "pass").newCall(request()).execute()
        response.use { assertThat(it.code).isEqualTo(401) }
        assertThat(server.requestCount).isEqualTo(1)
    }

    @Test fun `200 on first request does not trigger auth`() {
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))
        val response = client("user", "pass").newCall(request()).execute()
        response.use {
            assertThat(it.code).isEqualTo(200)
            assertThat(it.body?.string()).isEqualTo("ok")
        }
        assertThat(server.requestCount).isEqualTo(1)
    }

    private fun parseDigestParams(s: String): Map<String, String> {
        val result = LinkedHashMap<String, String>()
        var i = 0
        val n = s.length
        while (i < n) {
            while (i < n && (s[i].isWhitespace() || s[i] == ',')) i++
            if (i >= n) break
            val keyStart = i
            while (i < n && s[i] != '=' && s[i] != ',') i++
            if (i >= n || s[i] != '=') break
            val key = s.substring(keyStart, i).trim()
            i++
            while (i < n && s[i].isWhitespace()) i++
            if (i >= n) break
            val value: String = if (s[i] == '"') {
                i++
                val sb = StringBuilder()
                while (i < n && s[i] != '"') {
                    if (s[i] == '\\' && i + 1 < n) { sb.append(s[i + 1]); i += 2 }
                    else { sb.append(s[i]); i++ }
                }
                if (i < n) i++
                sb.toString()
            } else {
                val v = i
                while (i < n && s[i] != ',') i++
                s.substring(v, i).trim()
            }
            result[key] = value
        }
        return result
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

    private companion object {
        private val HEX = "0123456789abcdef".toCharArray()
    }
}
