package com.heathen.hadeswatch.features.k0reader

class LocalK0ReaderAdapter : K0ReaderAdapter {
    private val engine = RsvpReaderEngine()

    override fun loadText(text: String) = engine.loadText(text)
    override fun setWordsPerMinute(wpm: Int) = engine.setWordsPerMinute(wpm)
    override fun setChunkSize(chunkSize: Int) = engine.setChunkSize(chunkSize)
    override fun currentToken(): String = engine.currentToken()
    override fun nextToken(): String? = engine.nextToken()
    override fun previousToken(): String? = engine.previousToken()
    override fun reset() = engine.reset()
    override fun tokenCount(): Int = engine.tokenCount()
    override fun currentIndex(): Int = engine.currentIndex()
    override fun tokenAt(index: Int): String? = engine.tokenAt(index)
    override fun rewind(steps: Int) = engine.rewind(steps)
    override fun intervalMillis(): Long = engine.intervalMillis()
    override fun progressPercent(): Float {
        if (engine.tokenCount() == 0) return 0f
        return ((engine.currentIndex() + 1).toFloat() / engine.tokenCount()).coerceIn(0f, 1f)
    }
    override fun hasText(): Boolean = engine.tokenCount() > 0

    fun engineRef(): RsvpReaderEngine = engine
}
