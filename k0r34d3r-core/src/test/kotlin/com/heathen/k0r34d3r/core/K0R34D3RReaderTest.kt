package com.heathen.k0r34d3r.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class K0R34D3RReaderTest {

    private fun reader(): K0R34D3RReader = K0R34D3RReader()

    @Test
    fun emptyTextProducesNoTokens() {
        val core = reader()
        core.loadText("")
        assertEquals(0, core.tokenCount())
        assertFalse(core.hasText())
        assertEquals("", core.currentToken())
        assertEquals(0f, core.progressPercent())
    }

    @Test
    fun whitespaceNormalization() {
        val core = reader()
        core.loadText("  hello   world\r\n\r\nfrom   field  ")
        assertEquals(4, core.tokenCount())
        assertEquals("hello", core.currentToken())
    }

    @Test
    fun punctuationPreservedInTokens() {
        val core = reader()
        core.loadText("Signal lost. Retry uplink!")
        assertEquals("Signal", core.currentToken())
        core.nextToken()
        assertEquals("lost.", core.currentToken())
    }

    @Test
    fun chunkSizeOneSplitsByWord() {
        val core = reader()
        core.setChunkSize(1)
        core.loadText("one two three")
        assertEquals(3, core.tokenCount())
        assertEquals("one", core.currentToken())
    }

    @Test
    fun chunkSizeTwoGroupsWords() {
        val core = reader()
        core.setChunkSize(2)
        core.loadText("one two three four")
        assertEquals(2, core.tokenCount())
        assertEquals("one two", core.currentToken())
        core.nextToken()
        assertEquals("three four", core.currentToken())
    }

    @Test
    fun nextAndPreviousToken() {
        val core = reader()
        core.loadText("alpha beta gamma")
        assertEquals("alpha", core.currentToken())
        assertEquals("beta", core.nextToken())
        assertEquals("gamma", core.nextToken())
        assertNull(core.nextToken())
        assertEquals("beta", core.previousToken())
    }

    @Test
    fun resetReturnsToStart() {
        val core = reader()
        core.loadText("alpha beta gamma")
        core.nextToken()
        core.nextToken()
        core.reset()
        assertEquals(0, core.currentIndex())
        assertEquals("alpha", core.currentToken())
    }

    @Test
    fun progressPercentAdvances() {
        val core = reader()
        core.loadText("one two three four")
        assertEquals(0.25f, core.progressPercent())
        core.nextToken()
        assertEquals(0.5f, core.progressPercent())
    }

    @Test
    fun tokenCountAndCurrentIndex() {
        val core = reader()
        core.loadText("a b c")
        assertEquals(3, core.tokenCount())
        assertEquals(0, core.currentIndex())
        core.nextToken()
        assertEquals(1, core.currentIndex())
    }

    @Test
    fun phraseChunkingUsesPunctuationBoundaries() {
        val core = reader()
        core.setChunkSize(4)
        core.loadText("First sentence. Second sentence here.")
        assertTrue(core.tokenCount() >= 2)
        assertTrue(core.currentToken().contains("First") || core.currentToken().contains("sentence"))
    }
}
