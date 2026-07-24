package xa.refile.core.webdav

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

/**
 * WebDAV PROPFIND multistatus (207) 响应解析器（计划 §M1 SubTask 1.2.2）。
 *
 * 使用 [XmlPullParserFactory.newInstance()] 创建 parser（kxml2 的 KXmlParser 会作为实现被加载），
 * 解析 `D:response` 内的 `D:href`、`D:propstat/D:prop/D:{displayname,getcontentlength,
 * getlastmodified,creationdate,resourcetype,getcontenttype}`。
 *
 * 红线：只用 XmlPullParser 流式解析（不使用 DOM）。
 *
 * WebDAV 默认命名空间 `DAV:`。解析按 local name 匹配（兼容不同前缀 D/d/默认），
 * 仅当属性值非空时才覆盖（避免多 propstat 中 404 段清空已得到的值）。
 */
class PropfindParser {

    fun parse(xml: String): List<WebDavEntry> {
        if (xml.isBlank()) return emptyList()
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val pp = factory.newPullParser()
        pp.setInput(StringReader(xml))
        val results = mutableListOf<WebDavEntry>()
        try {
            var event = pp.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && pp.name == "response") {
                    results.add(parseResponse(pp))
                }
                event = pp.next()
            }
        } catch (e: XmlPullParserException) {
            // 损坏的 XML：返回已解析的部分。
            return results
        } catch (e: IOException) {
            return results
        }
        return results
    }

    /**
     * 假定 pp 位于 START_TAG `response`，解析该 response 直至其 END_TAG。
     */
    private fun parseResponse(pp: XmlPullParser): WebDavEntry {
        val startDepth = pp.depth
        var href: String? = null
        var displayName: String? = null
        var isCollection = false
        var contentLength: Long? = null
        var lastModified: String? = null
        var creationDate: String? = null
        var contentType: String? = null

        var event = pp.next()
        while (!(event == XmlPullParser.END_TAG &&
                    pp.name == "response" && pp.depth == startDepth) &&
            event != XmlPullParser.END_DOCUMENT
        ) {
            if (event == XmlPullParser.START_TAG) {
                when (pp.name) {
                    "href" -> readText(pp)?.let { href = it }
                    "displayname" -> readText(pp)?.let { displayName = it }
                    "getcontentlength" -> readText(pp)?.toLongOrNull()?.let { contentLength = it }
                    "getlastmodified" -> readText(pp)?.let { lastModified = it }
                    "creationdate" -> readText(pp)?.let { creationDate = it }
                    "getcontenttype" -> readText(pp)?.let { contentType = it }
                    "resourcetype" -> if (parseResourcetype(pp)) isCollection = true
                    else -> Unit // 忽略未知元素
                }
            }
            event = pp.next()
        }
        return WebDavEntry(
            href = href ?: "",
            displayName = displayName,
            isCollection = isCollection,
            contentLength = contentLength,
            lastModified = lastModified,
            creationDate = creationDate,
            contentType = contentType,
        )
    }

    /**
     * 假定 pp 位于 START_TAG `resourcetype`，遍历其子元素，存在 `collection` 即为目录。
     */
    private fun parseResourcetype(pp: XmlPullParser): Boolean {
        val startDepth = pp.depth
        var found = false
        var event = pp.next()
        while (!(event == XmlPullParser.END_TAG &&
                    pp.name == "resourcetype" && pp.depth == startDepth) &&
            event != XmlPullParser.END_DOCUMENT
        ) {
            if (event == XmlPullParser.START_TAG && pp.name == "collection") {
                found = true
            }
            event = pp.next()
        }
        return found
    }

    /**
     * 假定 pp 位于 START_TAG，读取其文本内容；空/仅空白返回 null。
     * nextText() 调用后 pp 停留在对应 END_TAG。
     */
    private fun readText(pp: XmlPullParser): String? {
        return try {
            val text = pp.nextText()
            text.takeIf { it.isNotBlank() }
        } catch (e: XmlPullParserException) {
            null
        } catch (e: IOException) {
            null
        }
    }
}
