package com.heathen.hadeswatch.features.k0reader

interface K0ReaderAdapter {
    fun loadText(text: String)
    fun setWordsPerMinute(wpm: Int)
    fun setChunkSize(chunkSize: Int)
    fun currentToken(): String
    fun nextToken(): String?
    fun previousToken(): String?
    fun reset()
    fun tokenCount(): Int
    fun currentIndex(): Int
    fun tokenAt(index: Int): String?
    fun rewind(steps: Int = 10)
    fun intervalMillis(): Long
    fun progressPercent(): Float
    fun hasText(): Boolean
}
