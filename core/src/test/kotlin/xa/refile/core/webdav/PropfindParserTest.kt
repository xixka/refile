package xa.refile.core.webdav

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PropfindParserTest {

    private val parser = PropfindParser()

    /** 真实样例：一个目录 + 一个文件 + 一个带中文显示名的项。 */
    private val sample = """<?xml version="1.0" encoding="utf-8"?>
        |<D:multistatus xmlns:D="DAV:">
        |  <D:response>
        |    <D:href>/dav/Movies/</D:href>
        |    <D:propstat>
        |      <D:prop>
        |        <D:displayname>Movies</D:displayname>
        |        <D:resourcetype><D:collection/></D:resourcetype>
        |        <D:getlastmodified>Mon, 01 Jan 2024 00:00:00 GMT</D:getlastmodified>
        |        <D:creationdate>2024-01-01T00:00:00Z</D:creationdate>
        |      </D:prop>
        |      <D:status>HTTP/1.1 200 OK</D:status>
        |    </D:propstat>
        |  </D:response>
        |  <D:response>
        |    <D:href>/dav/Movies/movie.mkv</D:href>
        |    <D:propstat>
        |      <D:prop>
        |        <D:displayname>movie.mkv</D:displayname>
        |        <D:getcontentlength>1024000</D:getcontentlength>
        |        <D:getlastmodified>Tue, 02 Jan 2024 12:00:00 GMT</D:getlastmodified>
        |        <D:creationdate>2024-01-02T12:00:00Z</D:creationdate>
        |        <D:resourcetype/>
        |        <D:getcontenttype>video/x-matroska</D:getcontenttype>
        |      </D:prop>
        |      <D:status>HTTP/1.1 200 OK</D:status>
        |    </D:propstat>
        |  </D:response>
        |  <D:response>
        |    <D:href>/dav/Movies/%E7%94%B5%E5%BD%B1.mkv</D:href>
        |    <D:propstat>
        |      <D:prop>
        |        <D:displayname>电影.mkv</D:displayname>
        |        <D:getcontentlength>2048</D:getcontentlength>
        |        <D:resourcetype/>
        |      </D:prop>
        |      <D:status>HTTP/1.1 200 OK</D:status>
        |    </D:propstat>
        |  </D:response>
        |</D:multistatus>""".trimMargin()

    @Test fun `parses three entries from sample`() {
        val entries = parser.parse(sample)
        assertThat(entries).hasSize(3)
    }

    @Test fun `first entry is collection with displayname and timestamps`() {
        val first = parser.parse(sample).first()
        assertThat(first.href).isEqualTo("/dav/Movies/")
        assertThat(first.displayName).isEqualTo("Movies")
        assertThat(first.isCollection).isTrue()
        assertThat(first.contentLength).isNull()
        assertThat(first.lastModified).isEqualTo("Mon, 01 Jan 2024 00:00:00 GMT")
        assertThat(first.creationDate).isEqualTo("2024-01-01T00:00:00Z")
        assertThat(first.contentType).isNull()
    }

    @Test fun `file entry has length contenttype and is not collection`() {
        val file = parser.parse(sample)[1]
        assertThat(file.href).isEqualTo("/dav/Movies/movie.mkv")
        assertThat(file.displayName).isEqualTo("movie.mkv")
        assertThat(file.isCollection).isFalse()
        assertThat(file.contentLength).isEqualTo(1024000L)
        assertThat(file.contentType).isEqualTo("video/x-matroska")
        assertThat(file.lastModified).isEqualTo("Tue, 02 Jan 2024 12:00:00 GMT")
    }

    @Test fun `chinese displayname entry parses correctly`() {
        val cn = parser.parse(sample)[2]
        assertThat(cn.displayName).isEqualTo("电影.mkv")
        assertThat(cn.isCollection).isFalse()
        assertThat(cn.contentLength).isEqualTo(2048L)
        // 缺失属性应为 null
        assertThat(cn.lastModified).isNull()
        assertThat(cn.creationDate).isNull()
        assertThat(cn.contentType).isNull()
        assertThat(cn.extractExtension()).isEqualTo("mkv")
    }

    @Test fun `collection entry extension is null`() {
        val first = parser.parse(sample).first()
        assertThat(first.extractExtension()).isNull()
    }

    @Test fun `empty string returns empty list`() {
        assertThat(parser.parse("")).isEmpty()
        assertThat(parser.parse("   ")).isEmpty()
    }

    @Test fun `entry with only href yields defaults`() {
        val xml = """<?xml version="1.0"?><D:multistatus xmlns:D="DAV:">
            |  <D:response><D:href>/dav/only</D:href></D:response>
            |</D:multistatus>""".trimMargin()
        val e = parser.parse(xml).single()
        assertThat(e.href).isEqualTo("/dav/only")
        assertThat(e.displayName).isNull()
        assertThat(e.isCollection).isFalse()
        assertThat(e.contentLength).isNull()
    }

    @Test fun `empty resourcetype means not collection`() {
        val xml = """<?xml version="1.0"?><D:multistatus xmlns:D="DAV:">
            |  <D:response>
            |    <D:href>/dav/file</D:href>
            |    <D:propstat><D:prop><D:resourcetype/></D:prop></D:propstat>
            |  </D:response>
            |</D:multistatus>""".trimMargin()
        assertThat(parser.parse(xml).single().isCollection).isFalse()
    }

    @Test fun `resourcetype with collection child means directory`() {
        val xml = """<?xml version="1.0"?><D:multistatus xmlns:D="DAV:">
            |  <D:response>
            |    <D:href>/dav/dir/</D:href>
            |    <D:propstat><D:prop><D:resourcetype><D:collection/></D:resourcetype></D:prop></D:propstat>
            |  </D:response>
            |</D:multistatus>""".trimMargin()
        assertThat(parser.parse(xml).single().isCollection).isTrue()
    }

    @Test fun `default namespace without prefix parses`() {
        val xml = """<?xml version="1.0"?><multistatus xmlns="DAV:">
            |  <response>
            |    <href>/dav/x</href>
            |    <propstat><prop>
            |      <displayname>x</displayname>
            |      <getcontentlength>10</getcontentlength>
            |    </prop></propstat>
            |  </response>
            |</multistatus>""".trimMargin()
        val e = parser.parse(xml).single()
        assertThat(e.href).isEqualTo("/dav/x")
        assertThat(e.displayName).isEqualTo("x")
        assertThat(e.contentLength).isEqualTo(10L)
    }

    @Test fun `malformed xml returns empty list`() {
        assertThat(parser.parse("not xml at all <broken")).isEmpty()
    }

    @Test fun `non numeric contentlength yields null`() {
        val xml = """<?xml version="1.0"?><D:multistatus xmlns:D="DAV:">
            |  <D:response>
            |    <D:href>/dav/x</D:href>
            |    <D:propstat><D:prop><D:getcontentlength>not-a-number</D:getcontentlength></D:prop></D:propstat>
            |  </D:response>
            |</D:multistatus>""".trimMargin()
        assertThat(parser.parse(xml).single().contentLength).isNull()
    }

    @Test fun `blank displayname treated as missing`() {
        val xml = """<?xml version="1.0"?><D:multistatus xmlns:D="DAV:">
            |  <D:response>
            |    <D:href>/dav/x</D:href>
            |    <D:propstat><D:prop><D:displayname>   </D:displayname></D:prop></D:propstat>
            |  </D:response>
            |</D:multistatus>""".trimMargin()
        assertThat(parser.parse(xml).single().displayName).isNull()
    }

    @Test fun `extractExtension handles no extension`() {
        val e = WebDavEntry(href = "/dav/README", displayName = "README", isCollection = false)
        assertThat(e.extractExtension()).isNull()
    }

    @Test fun `extractExtension lowercases extension`() {
        val e = WebDavEntry(href = "/dav/a.MKV", displayName = "a.MKV", isCollection = false)
        assertThat(e.extractExtension()).isEqualTo("mkv")
    }
}
