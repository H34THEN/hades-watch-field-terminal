package com.heathen.k0r34d3r.core

class K0R34D3RReader(
    initialConfig: K0R34D3RReaderConfig = K0R34D3RReaderConfig(),
) : K0R34D3RCore {

    private var config = initialConfig
    private var tokens: List<String> = emptyList()
    private var index: Int = 0
    private var sourceText: String = ""

    override fun loadText(text: String) {
        sourceText = text
        tokens = K0R34D3RChunker.buildTokens(text, config.chunkSize)
        index = 0
    }

    override fun setWordsPerMinute(wpm: Int) {
        config = config.copy(
            wordsPerMinute = wpm.coerceIn(
                K0R34D3RReaderConfig.MIN_WPM,
                K0R34D3RReaderConfig.MAX_WPM,
            ),
        )
    }

    override fun setChunkSize(chunkSize: Int) {
        val clamped = chunkSize.coerceIn(1, K0R34D3RReaderConfig.MAX_CHUNK_SIZE)
        if (clamped == config.chunkSize) return
        config = config.copy(chunkSize = clamped)
        if (sourceText.isNotEmpty()) {
            val previousWordIndex = K0R34D3RChunker.wordIndexForChunkIndex(tokens, index)
            tokens = K0R34D3RChunker.buildTokens(sourceText, config.chunkSize)
            index = K0R34D3RChunker.chunkIndexForWordIndex(tokens, previousWordIndex)
        }
    }

    override fun setFontScale(fontScale: Float) {
        config = config.copy(fontScale = fontScale.coerceIn(0.5f, 3.0f))
    }

    override fun currentToken(): String = tokens.getOrElse(index) { "" }

    override fun nextToken(): String? {
        if (tokens.isEmpty() || index >= tokens.lastIndex) return null
        index++
        return currentToken()
    }

    override fun previousToken(): String? {
        if (tokens.isEmpty() || index <= 0) return null
        index--
        return currentToken()
    }

    override fun reset() {
        index = 0
    }

    override fun tokenCount(): Int = tokens.size

    override fun currentIndex(): Int = index

    override fun hasText(): Boolean = tokens.isNotEmpty()

    override fun progressPercent(): Float {
        if (tokens.isEmpty()) return 0f
        return ((index + 1).toFloat() / tokens.size.toFloat()).coerceIn(0f, 1f)
    }

    override fun readingState(): K0R34D3RReadingState =
        K0R34D3RReadingState(tokens = tokens, currentIndex = index, config = config)

    override fun intervalMillis(): Long {
        val safeWpm = config.wordsPerMinute.coerceAtLeast(1)
        return 60_000L / safeWpm
    }

    override fun rewind(steps: Int) {
        index = (index - steps).coerceAtLeast(0)
    }

    fun estimatedSecondsRemaining(): Int {
        val wpm = config.wordsPerMinute
        if (wpm <= 0 || tokens.isEmpty()) return 0
        val totalWords = K0R34D3RTokenizer.countWords(sourceText)
        val currentWordIndex = K0R34D3RChunker.wordIndexForChunkIndex(tokens, index)
        val remaining = (totalWords - currentWordIndex).coerceAtLeast(0)
        return ((remaining.toFloat() / wpm) * 60f).toInt().coerceAtLeast(0)
    }
}
