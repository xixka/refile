package com.webdavrenamer.core.webdav

import java.nio.charset.StandardCharsets

/**
 * UTF-8 URL 路径编码工具（计划 §M1 SubTask 1.2.4）。
 *
 * 提供：
 * - [encodePath]：对 URL 路径段做百分号编码（保留 `/`），中文/空格/特殊字符（如 `:`、`?`、`#`、`%`）编码。
 * - [buildDestinationUrl]：拼接 baseUrl 与编码后的 path，用于 MOVE 的 `Destination` 头完整编码。
 *
 * 红线：仅做路径编码，不读取文件内容；不涉及凭据。
 */
object UrlEncoding {

    /** RFC 3986 unreserved 字符集（A-Za-z0-9-._~）外加路径分隔符 `/`。 */
    private fun isSafeChar(c: Char): Boolean {
        return when {
            c in 'A'..'Z' -> true
            c in 'a'..'z' -> true
            c in '0'..'9' -> true
            c == '-' || c == '_' || c == '.' || c == '~' -> true
            c == '/' -> true
            else -> false
        }
    }

    /**
     * 对 URL 路径做百分号编码（保留 `/` 作为路径分隔符）。
     *
     * 中文、空格、特殊字符（如 `:`、`?`、`#`、`%`）按 UTF-8 字节编码为 `%XX`。
     */
    fun encodePath(path: String): String {
        if (path.isEmpty()) return ""
        val bytes = path.toByteArray(StandardCharsets.UTF_8)
        val sb = StringBuilder(bytes.size)
        var i = 0
        while (i < bytes.size) {
            val b = bytes[i]
            // b 为有符号 byte；还原为无符号值后再判断是否为 ASCII 安全字符
            val unsigned = b.toInt() and 0xFF
            val ch = unsigned.toChar()
            if (unsigned < 128 && isSafeChar(ch)) {
                sb.append(ch)
            } else {
                appendPercentEncoded(sb, unsigned)
            }
            i++
        }
        return sb.toString()
    }

    private fun appendPercentEncoded(sb: StringBuilder, byte: Int) {
        sb.append('%')
        sb.append(hexUpper((byte ushr 4) and 0x0F))
        sb.append(hexUpper(byte and 0x0F))
    }

    private fun hexUpper(nibble: Int): Char =
        if (nibble < 10) ('0' + nibble) else ('A' + (nibble - 10))

    /**
     * 拼接 baseUrl 与编码后的 path，用于 MOVE 的 `Destination` 头完整编码 URL。
     *
     * - baseUrl 末尾的 `/` 与 path 开头的 `/` 不会重复。
     * - path 中的中文/空格/特殊字符会被 [encodePath] 编码，`/` 保留。
     */
    fun buildDestinationUrl(baseUrl: String, path: String): String {
        val trimmedBase = baseUrl.trimEnd('/')
        val normalizedPath = if (path.isEmpty() || path == "/") {
            "/"
        } else {
            if (path.startsWith("/")) path else "/$path"
        }
        val encoded = encodePath(normalizedPath)
        return "$trimmedBase$encoded"
    }
}
