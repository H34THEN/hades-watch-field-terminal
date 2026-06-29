package com.heathen.k0r34d3r.core.epub

import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class EpubTextExtractorTest {

    @Test
    fun htmlToPlainTextStripsTags() {
        val html = "<p>Hello <strong>field</strong> terminal.</p>"
        val text = EpubTextExtractor.htmlToPlainText(html)
        assertEquals("Hello field terminal.", text)
    }

    @Test
    fun extractsMinimalEpub() {
        val epub = buildMinimalEpub(
            title = "Test Book",
            chapterHtml = "<html><body><p>Signal lost. Retry uplink!</p></body></html>",
        )
        val result = EpubTextExtractor.extract(epub, filename = "test.epub")
        assertEquals("Test Book", result.title)
        assertTrue(result.plainText.contains("Signal lost"))
        assertEquals(1, result.chapterCount)
    }

    @Test
    fun emptySpineThrows() {
        val epub = buildEpubWithoutSpine()
        assertFailsWith<EpubImportException> {
            EpubTextExtractor.extract(epub)
        }
    }

    @Test
    fun extractsMinimalEpubWithHrefBeforeId() {
        val epub = buildMinimalEpub(
            title = "Reordered",
            chapterHtml = "<html><body><p>Field terminal online.</p></body></html>",
            hrefBeforeId = true,
        )
        val result = EpubTextExtractor.extract(epub, filename = "reorder.epub")
        assertEquals("Reordered", result.title)
        assertTrue(result.plainText.contains("Field terminal online"))
        assertEquals(1, result.chapterCount)
    }

    private fun buildMinimalEpub(title: String, chapterHtml: String, hrefBeforeId: Boolean = false): ByteArray {
        val container = """
            <?xml version="1.0"?>
            <container version="1.0" xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
              <rootfiles>
                <rootfile full-path="OEBPS/content.opf" media-type="application/oebps-package+xml"/>
              </rootfiles>
            </container>
        """.trimIndent()
        val manifestItem = if (hrefBeforeId) {
            """<item href="chapter1.xhtml" id="ch1" media-type="application/xhtml+xml"/>"""
        } else {
            """<item id="ch1" href="chapter1.xhtml" media-type="application/xhtml+xml"/>"""
        }
        val opf = """
            <?xml version="1.0"?>
            <package xmlns="http://www.idpf.org/2007/opf" version="2.0">
              <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
                <dc:title>$title</dc:title>
              </metadata>
              <manifest>
                $manifestItem
              </manifest>
              <spine>
                <itemref idref="ch1"/>
              </spine>
            </package>
        """.trimIndent()
        return zipOf(
            "META-INF/container.xml" to container.toByteArray(),
            "OEBPS/content.opf" to opf.toByteArray(),
            "OEBPS/chapter1.xhtml" to chapterHtml.toByteArray(),
        )
    }

    private fun buildEpubWithoutSpine(): ByteArray {
        val container = """
            <?xml version="1.0"?>
            <container version="1.0" xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
              <rootfiles>
                <rootfile full-path="OEBPS/content.opf" media-type="application/oebps-package+xml"/>
              </rootfiles>
            </container>
        """.trimIndent()
        val opf = """
            <?xml version="1.0"?>
            <package xmlns="http://www.idpf.org/2007/opf" version="2.0">
              <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
                <dc:title>Empty</dc:title>
              </metadata>
              <manifest>
                <item id="ch1" href="chapter1.xhtml" media-type="application/xhtml+xml"/>
              </manifest>
              <spine></spine>
            </package>
        """.trimIndent()
        return zipOf(
            "META-INF/container.xml" to container.toByteArray(),
            "OEBPS/content.opf" to opf.toByteArray(),
        )
    }

    private fun zipOf(vararg entries: Pair<String, ByteArray>): ByteArray {
        val out = ByteArrayOutputStream()
        ZipOutputStream(out).use { zip ->
            entries.forEach { (name, data) ->
                zip.putNextEntry(ZipEntry(name))
                zip.write(data)
                zip.closeEntry()
            }
        }
        return out.toByteArray()
    }
}
