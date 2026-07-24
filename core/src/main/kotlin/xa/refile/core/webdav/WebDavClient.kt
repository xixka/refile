package xa.refile.core.webdav

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * 基于 OkHttp 的轻量 WebDAV 客户端（计划 §M1 SubTask 1.2.1）。
 *
 * 提供 PROPFIND（Depth 0/1）、MOVE、MKCOL、连接测试。重命名只通过 MOVE/MKCOL 完成，
 * 不读取/下载文件内容（红线）。
 *
 * OkHttp 同步调用由 [withContext]`[Dispatchers.IO]` 包裹，便于协程化使用。
 * OkHttp client 通过构造函数注入（便于测试注入指向 MockWebServer 的 client）；
 * AuthInterceptor 会在注入 client 基础上自动追加，实现 Basic/Digest 自动协商。
 *
 * 密码仅用于构造 [AuthInterceptor]，不落盘、不进入日志。
 *
 * @param baseUrl   服务器根 URL，如 `https://dav.example.com/`。
 * @param username  用户名（可空，匿名访问时为 null）。
 * @param password  密码（可空）。
 * @param client    可注入的 OkHttpClient（默认新建）；其配置会被保留并追加 AuthInterceptor。
 */
class WebDavClient(
    private val baseUrl: String,
    private val username: String?,
    private val password: String?,
    client: OkHttpClient = OkHttpClient(),
) {
    private val parser = PropfindParser()

    private val httpClient: OkHttpClient = client.newBuilder()
        .addInterceptor(AuthInterceptor(username, password))
        .build()

    /**
     * 发 PROPFIND 请求。
     * - Depth 0：只返回资源本身（1 条）。
     * - Depth 1：返回当前目录及其直接子项（第一项通常是当前目录本身）。
     *
     * 请求体为 WebDAV PROPFIND 标准 XML，请求 displayname/getcontentlength/
     * getlastmodified/creationdate/resourcetype/getcontenttype。
     *
     * 注意：本应用中 PROPFIND 始终作用于「目录」（浏览器列目录、预览冲突检测、
     * 伴随文件发现、连接测试 rootPath），因此 URL 一律补齐末尾 `/`。
     * 许多 WebDAV 服务器（Alist、nginx-based）对不带末尾斜杠的目录 URL 会返回
     * 301 重定向到带斜杠的 URL；OkHttp 在跟随 301/302 时会按 RFC 7231 把
     * PROPFIND 降级为 GET，导致拿不到 multistatus 响应、上层误判「服务器无响应」。
     * 主动补斜杠可从源头规避该重定向。
     */
    suspend fun propfind(path: String, depth: Int): List<WebDavEntry> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(buildCollectionUrl(path))
            .method("PROPFIND", PROPFIND_BODY.toRequestBody(XML_MEDIA_TYPE))
            .header("Depth", depth.toString())
            .header("Content-Type", "application/xml; charset=utf-8")
            .build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@use emptyList<WebDavEntry>()
            val xml = response.body?.string() ?: ""
            parser.parse(xml)
        }
    }

    /**
     * 发 MOVE 请求重命名/移动资源。
     * - `Destination` 头为完整 URL 编码（见 [UrlEncoding.buildDestinationUrl]）。
     * - `Overwrite` 头为 `F`（overwrite=false）或 `T`。
     * - 成功（2xx，典型 201/204）返回 true，其余状态码返回 false。
     */
    suspend fun move(fromPath: String, toPath: String, overwrite: Boolean = false): Boolean =
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(buildUrl(fromPath))
                .method("MOVE", null)
                .header("Destination", UrlEncoding.buildDestinationUrl(baseUrl, toPath))
                .header("Overwrite", if (overwrite) "T" else "F")
                .build()
            httpClient.newCall(request).execute().use { it.isSuccessful }
        }

    /**
     * 发 MKCOL 创建目录。405（已存在/不允许）视为幂等成功（返回 true）。
     * 201 返回 true，其余状态码返回 false。
     */
    suspend fun mkcol(path: String): Boolean = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(buildUrl(path))
            .method("MKCOL", null)
            .build()
        httpClient.newCall(request).execute().use { response ->
            when (response.code) {
                201 -> true
                405 -> true // 已存在/不允许 → 幂等成功
                else -> false
            }
        }
    }

    /**
     * 对 [path] 发 PROPFIND Depth: 0 测试连接，返回成功/失败原因。
     * - 网络异常 → [ConnectionResult.NetworkError]。
     * - 401 → [ConnectionResult.AuthFailure]（认证失败）。
     * - 405/501 → [ConnectionResult.NotWebDav]（PROPFIND 不被支持）。
     * - 207 或 2xx → [ConnectionResult.Success]（附带解析到的资源）。
     * - 其它 → [ConnectionResult.HttpError]。
     */
    suspend fun testConnection(path: String): ConnectionResult = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(buildCollectionUrl(path))
            .method("PROPFIND", PROPFIND_BODY.toRequestBody(XML_MEDIA_TYPE))
            .header("Depth", "0")
            .header("Content-Type", "application/xml; charset=utf-8")
            .build()
        try {
            httpClient.newCall(request).execute().use { response ->
                when {
                    response.code == 401 -> ConnectionResult.AuthFailure(response.code)
                    response.code == 207 || response.isSuccessful -> {
                        val xml = response.body?.string() ?: ""
                        val entry = parser.parse(xml).firstOrNull()
                        ConnectionResult.Success(entry)
                    }
                    response.code == 405 || response.code == 501 ->
                        ConnectionResult.NotWebDav(response.code)
                    else -> ConnectionResult.HttpError(response.code)
                }
            }
        } catch (e: IOException) {
            ConnectionResult.NetworkError(e.message ?: "network error")
        }
    }

    /** 拼接 baseUrl 与 UTF-8 编码后的 path。 */
    private fun buildUrl(path: String): String {
        val trimmedBase = baseUrl.trimEnd('/')
        val normalized = when {
            path.isEmpty() || path == "/" -> "/"
            path.startsWith("/") -> path
            else -> "/$path"
        }
        return trimmedBase + UrlEncoding.encodePath(normalized)
    }

    /**
     * 拼接 baseUrl 与 UTF-8 编码后的 path，并强制末尾 `/`。
     *
     * 用于 PROPFIND 等目录性请求：避免部分 WebDAV 服务器（Alist、nginx-based）
     * 对无末尾斜杠的目录 URL 返回 301 重定向，OkHttp 跟随 301 时会把 PROPFIND
     * 降级为 GET 而拿不到 multistatus 响应。
     */
    private fun buildCollectionUrl(path: String): String {
        val trimmedBase = baseUrl.trimEnd('/')
        val normalized = when {
            path.isEmpty() || path == "/" -> "/"
            path.startsWith("/") -> path
            else -> "/$path"
        }
        val withSlash = if (normalized.endsWith("/")) normalized else "$normalized/"
        return trimmedBase + UrlEncoding.encodePath(withSlash)
    }

    companion object {
        private val XML_MEDIA_TYPE = "application/xml; charset=utf-8".toMediaType()

        /** 标准 PROPFIND 请求体：请求 displayname/size/时间/resourcetype/contenttype。 */
        private val PROPFIND_BODY = """<?xml version="1.0" encoding="utf-8"?>
            |<D:propfind xmlns:D="DAV:">
            |  <D:prop>
            |    <D:displayname/>
            |    <D:getcontentlength/>
            |    <D:getlastmodified/>
            |    <D:creationdate/>
            |    <D:resourcetype/>
            |    <D:getcontenttype/>
            |  </D:prop>
            |</D:propfind>""".trimMargin()
    }
}

/**
 * 连接测试结果（计划 §M1 SubTask 1.2.1 testConnection）。
 */
sealed class ConnectionResult {
    /** 连接成功，附带 PROPFIND 解析到的根资源（可能为空）。 */
    data class Success(val entry: WebDavEntry? = null) : ConnectionResult()
    /** 认证失败（401）。 */
    data class AuthFailure(val code: Int = 401) : ConnectionResult()
    /** 非 WebDAV（PROPFIND 不被支持，405/501）。 */
    data class NotWebDav(val code: Int) : ConnectionResult()
    /** 其它 HTTP 错误。 */
    data class HttpError(val code: Int) : ConnectionResult()
    /** 网络错误（无法连接）。 */
    data class NetworkError(val message: String) : ConnectionResult()
}
