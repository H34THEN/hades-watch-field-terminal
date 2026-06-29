package com.heathen.k0r34d3r.core

interface K0R34D3RCore {
    fun loadText(text: String)
    fun setWordsPerMinute(wpm: Int)
    fun setChunkSize(chunkSize: Int)
    fun setFontScale(fontScale: Float)
    fun currentToken(): String
    fun nextToken(): String?
    fun previousToken(): String?
    fun reset()
    fun tokenCount(): Int
    fun currentIndex(): Int
    fun hasText(): Boolean
    fun progressPercent(): Float
    fun readingState(): K0R34D3RReadingState
    fun intervalMillis(): Long
    fun rewind(steps: Int = 10)
}
