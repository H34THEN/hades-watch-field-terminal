package com.heathen.k0r34d3r.core

import kotlin.test.Test
import kotlin.test.assertEquals

class K0R34D3RGoldenFixtureTest {

    private fun loadFixture(name: String): String =
        javaClass.getResourceAsStream("/golden/$name.txt")!!
            .bufferedReader()
            .readText()

    private fun loadExpected(name: String): List<String> =
        javaClass.getResourceAsStream("/golden/expected/$name")!!
            .bufferedReader()
            .readLines()
            .filter { it.isNotEmpty() || it == "" }
            .let { lines ->
                // Preserve trailing empty only if file ends with newline on empty line - trim trailing blank lines
                lines.dropLastWhile { it.isEmpty() }
            }

    private fun assertGolden(fixture: String, chunkSize: Int, expectedSuffix: String) {
        val text = loadFixture(fixture)
        val expected = loadExpected("$fixture.$expectedSuffix.expected")
        val actual = K0R34D3RChunker.buildTokens(text, chunkSize)
        assertEquals(expected, actual, "Golden mismatch for $fixture ($expectedSuffix)")
    }

    @Test
    fun simpleSentenceChunk1() = assertGolden("simple_sentence", 1, "chunk1")

    @Test
    fun simpleSentenceChunk2() = assertGolden("simple_sentence", 2, "chunk2")

    @Test
    fun simpleSentenceChunk3() = assertGolden("simple_sentence", 3, "chunk3")

    @Test
    fun simpleSentencePhraseMode() = assertGolden("simple_sentence", 4, "phrase")

    @Test
    fun punctuationSentenceChunk1() = assertGolden("punctuation_sentence", 1, "chunk1")

    @Test
    fun punctuationSentenceChunk2() = assertGolden("punctuation_sentence", 2, "chunk2")

    @Test
    fun punctuationSentenceChunk3() = assertGolden("punctuation_sentence", 3, "chunk3")

    @Test
    fun punctuationSentencePhraseMode() = assertGolden("punctuation_sentence", 4, "phrase")

    @Test
    fun whitespaceTabsNewlinesChunk1() = assertGolden("whitespace_tabs_newlines", 1, "chunk1")

    @Test
    fun unicodeSentenceChunk1() = assertGolden("unicode_sentence", 1, "chunk1")

    @Test
    fun phraseModeSamplePhraseMode() = assertGolden("phrase_mode_sample", 4, "phrase")
}
