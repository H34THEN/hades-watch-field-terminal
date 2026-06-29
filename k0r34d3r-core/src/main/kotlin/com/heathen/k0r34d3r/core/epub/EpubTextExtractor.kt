package com.heathen.k0r34d3r.core.epub

import com.heathen.k0r34d3r.core.K0R34D3RTokenizer
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Minimal local EPUB text extractor (ZIP + OPF spine + HTML strip).
 * Ported from K0R34D3R Flutter [EpubService] behavior — no DRM, no network.
 */
object EpubTextExtractor {

    fun extract(bytes: ByteArray, filename: String? = null): EpubParseResult =
        extract(ByteArrayInputStream(bytes), filename)

    fun extract(inputStream: InputStream, filename: String? = null): EpubParseResult {
        val entries = readZipEntries(inputStream)
        if (entries.isEmpty()) throw EpubImportException("EPUB archive is empty or unreadable.")

        val containerXml = findEntry(entries, "META-INF/container.xml")
            ?: throw EpubImportException("EPUB is missing META-INF/container.xml.")

        val opfPath = parseContainerRootPath(containerXml)
            ?: throw EpubImportException("EPUB container.xml has no valid rootfile path.")

        val opfXml = findEntryBySuffix(entries, opfPath)
            ?: throw EpubImportException("EPUB package file not found: $opfPath")

        val opfDir = opfPath.substringBeforeLast('/', "")
        val manifest = parseManifest(opfXml)
        val spineIds = parseSpineItemRefs(opfXml)
        if (spineIds.isEmpty()) throw EpubImportException("EPUB contains no readable spine items.")

        val metadataTitle = parseMetadataTitle(opfXml)
        val fullText = StringBuilder()
        var chapterCount = 0

        spineIds.forEachIndexed { index, id ->
            val href = manifest[id] ?: return@forEachIndexed
            val chapterPath = resolvePath(opfDir, href)
            val htmlBytes = findEntryBySuffix(entries, chapterPath) ?: return@forEachIndexed
            val html = htmlBytes.toString(Charsets.UTF_8)
            val text = htmlToPlainText(html)
            if (text.isBlank()) return@forEachIndexed

            chapterCount++
            val chapterTitle = "Chapter ${index + 1}"
            if (fullText.isNotEmpty()) fullText.append("\n\n")
            fullText.append(chapterTitle).append("\n\n").append(text)
        }

        val plainText = K0R34D3RTokenizer.normalizeWhitespace(fullText.toString())
        if (plainText.isBlank()) {
            throw EpubImportException(
                "EPUB contains no extractable text. DRM-protected books are not supported.",
            )
        }

        val title = metadataTitle?.takeIf { it.isNotBlank() }
            ?: titleFromFilename(filename)

        return EpubParseResult(
            title = title,
            plainText = plainText,
            chapterCount = chapterCount,
        )
    }

    private fun readZipEntries(inputStream: InputStream): Map<String, ByteArray> {
        val map = linkedMapOf<String, ByteArray>()
        ZipInputStream(inputStream).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                if (!entry.isDirectory) {
                    map[entry.name] = zip.readBytes()
                }
                zip.closeEntry()
            }
        }
        return map
    }

    private fun findEntry(entries: Map<String, ByteArray>, path: String): ByteArray? =
        entries[path] ?: entries.entries.firstOrNull { it.key.equals(path, ignoreCase = true) }?.value

    private fun findEntryBySuffix(entries: Map<String, ByteArray>, path: String): ByteArray? {
        findEntry(entries, path)?.let { return it }
        val normalized = path.replace('\\', '/')
        return entries.entries.firstOrNull {
            it.key.replace('\\', '/').endsWith(normalized, ignoreCase = true)
        }?.value
    }

    private fun parseContainerRootPath(xml: ByteArray): String? {
        val text = xml.toString(Charsets.UTF_8)
        val match = Regex("""full-path="([^"]+)"""", RegexOption.IGNORE_CASE).find(text)
        return match?.groupValues?.getOrNull(1)?.trim()
    }

    private fun parseManifest(opfXml: ByteArray): Map<String, String> {
        val text = opfXml.toString(Charsets.UTF_8)
        val items = Regex("""<item\s+[^>]*id="([^"]+)"[^>]*href="([^"]+)"[^>]*/?>""", RegexOption.IGNORE_CASE)
            .findAll(text)
        return items.associate { it.groupValues[1] to it.groupValues[2] }
    }

    private fun parseSpineItemRefs(opfXml: ByteArray): List<String> {
        val text = opfXml.toString(Charsets.UTF_8)
        return Regex("""<itemref\s+[^>]*idref="([^"]+)"[^>]*/?>""", RegexOption.IGNORE_CASE)
            .findAll(text)
            .map { it.groupValues[1] }
            .toList()
    }

    private fun parseMetadataTitle(opfXml: ByteArray): String? {
        val text = opfXml.toString(Charsets.UTF_8)
        return Regex("""<dc:title[^>]*>([^<]+)</dc:title>""", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.getOrNull(1)?.trim()
    }

    private fun resolvePath(baseDir: String, href: String): String {
        val cleanHref = href.substringBefore('#')
        return if (baseDir.isEmpty()) cleanHref else "$baseDir/$cleanHref"
    }

    internal fun htmlToPlainText(html: String): String {
        if (html.isBlank()) return ""
        var text = html
            .replace(Regex("""<br\s*/?>""", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("""</p>""", RegexOption.IGNORE_CASE), "\n\n")
            .replace(Regex("""</h[1-6]>""", RegexOption.IGNORE_CASE), "\n\n")
            .replace(Regex("<[^>]+>"), " ")
        text = text
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
        return K0R34D3RTokenizer.normalizeWhitespace(text)
    }

    private fun titleFromFilename(filename: String?): String {
        if (filename.isNullOrBlank()) return "EPUB Document"
        val name = filename.substringAfterLast('/').substringAfterLast('\\')
        return if (name.lowercase().endsWith(".epub")) name.dropLast(5) else name
    }
}
