package com.heathen.hadeswatch.features.k0reader

import com.heathen.k0r34d3r.core.K0R34D3RReader

/**
 * App adapter wrapping the shared :k0r34d3r-core Kotlin module.
 * Default reader engine for k0R34DER in the Field Terminal.
 */
class K0SdkReaderAdapter(
    private val core: K0R34D3RReader = K0R34D3RReader(),
) : K0ReaderAdapter {

    override fun loadText(text: String) = core.loadText(text)
    override fun setWordsPerMinute(wpm: Int) = core.setWordsPerMinute(wpm)
    override fun setChunkSize(chunkSize: Int) = core.setChunkSize(chunkSize)
    override fun currentToken(): String = core.currentToken()
    override fun nextToken(): String? = core.nextToken()
    override fun previousToken(): String? = core.previousToken()
    override fun reset() = core.reset()
    override fun tokenCount(): Int = core.tokenCount()
    override fun currentIndex(): Int = core.currentIndex()
    override fun rewind(steps: Int) = core.rewind(steps)
    override fun intervalMillis(): Long = core.intervalMillis()
    override fun progressPercent(): Float = core.progressPercent()
    override fun hasText(): Boolean = core.hasText()

    fun coreRef(): K0R34D3RReader = core
}
