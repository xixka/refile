package com.webdavrenamer.core.webdav

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UrlEncodingTest {

    @Test fun `chinese characters percent-encoded as UTF-8`() {
        // 电影 → UTF-8: E7 94 B5 E5 BD B1
        assertThat(UrlEncoding.encodePath("电影")).isEqualTo("%E7%94%B5%E5%BD%B1")
    }

    @Test fun `space encoded as percent-20 not plus`() {
        assertThat(UrlEncoding.encodePath("a b")).isEqualTo("a%20b")
    }

    @Test fun `slash preserved as path separator`() {
        assertThat(UrlEncoding.encodePath("/Movies/Series 01/")).isEqualTo("/Movies/Series%2001/")
    }

    @Test fun `special chars colon question hash percent encoded`() {
        assertThat(UrlEncoding.encodePath("a:b?c#d")).isEqualTo("a%3Ab%3Fc%23d")
    }

    @Test fun `percent sign encoded`() {
        assertThat(UrlEncoding.encodePath("50%off")).isEqualTo("50%25off")
    }

    @Test fun `unreserved chars preserved`() {
        assertThat(UrlEncoding.encodePath("A-z_0.1~9-")).isEqualTo("A-z_0.1~9-")
    }

    @Test fun `empty path returns empty`() {
        assertThat(UrlEncoding.encodePath("")).isEmpty()
    }

    @Test fun `mixed chinese space and slash`() {
        assertThat(UrlEncoding.encodePath("/媒体/电影 2024/")).isEqualTo(
            "/%E5%AA%92%E4%BD%93/%E7%94%B5%E5%BD%B1%202024/",
        )
    }

    @Test fun `buildDestinationUrl encodes path and joins base`() {
        val url = UrlEncoding.buildDestinationUrl("https://dav.example.com/dav/", "/Movies/电影.mkv")
        assertThat(url).isEqualTo("https://dav.example.com/dav/Movies/%E7%94%B5%E5%BD%B1.mkv")
    }

    @Test fun `buildDestinationUrl normalizes trailing slash and leading slash`() {
        val url = UrlEncoding.buildDestinationUrl("https://dav.example.com/dav", "Movies/a b")
        assertThat(url).isEqualTo("https://dav.example.com/dav/Movies/a%20b")
    }

    @Test fun `buildDestinationUrl root path`() {
        val url = UrlEncoding.buildDestinationUrl("https://dav.example.com/dav/", "/")
        assertThat(url).isEqualTo("https://dav.example.com/dav/")
    }

    @Test fun `buildDestinationUrl encodes special chars`() {
        val url = UrlEncoding.buildDestinationUrl("https://dav.example.com", "/a?b#c:d")
        assertThat(url).isEqualTo("https://dav.example.com/a%3Fb%23c%3Ad")
    }
}
