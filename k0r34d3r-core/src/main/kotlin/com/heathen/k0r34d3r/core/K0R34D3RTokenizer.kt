package com.heathen.k0r34d3r.core

/**
 * Text normalization and word splitting ported from K0R34D3R
 * [TextProcessingService] (Flutter/Dart).
 */
object K0R34D3RTokenizer {

    fun normalizeWhitespace(text: String): String {
        if (text.isEmpty()) return ""
        var normalized = text.replace("\r\n", "\n").replace('\r', '\n')
        normalized = normalized.replace(Regex("[ \\t]+"), " ")
        normalized = normalized.replace(Regex("\n{3,}"), "\n\n")
        return normalized.trim()
    }

    fun splitIntoWords(text: String): List<String> {
        val normalized = normalizeWhitespace(text)
        if (normalized.isEmpty()) return emptyList()
        return normalized.split(Regex("\\s+")).filter { it.isNotEmpty() }
    }

    fun countWords(text: String): Int = splitIntoWords(text).size
}
