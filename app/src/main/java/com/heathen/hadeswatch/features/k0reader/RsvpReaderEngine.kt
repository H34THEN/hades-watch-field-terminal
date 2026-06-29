package com.heathen.hadeswatch.features.k0reader

enum class ChunkSizeMode(val words: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    PHRASE(-1),
}

class RsvpReaderEngine : K0ReaderAdapter {
    private var tokens: List<String> = emptyList()
    private var index: Int = 0
    private var wpm: Int = 300
    private var chunkSize: Int = 1

    override fun loadText(text: String) {
        tokens = buildChunks(normalize(text), chunkSize)
        index = 0
    }

    override fun setWordsPerMinute(wpm: Int) {
        this.wpm = wpm.coerceIn(100, 1000)
    }

    override fun setChunkSize(chunkSize: Int) {
        this.chunkSize = chunkSize.coerceIn(1, 8)
    }

    override fun currentToken(): String = tokens.getOrElse(index) { "" }

    override fun nextToken(): String? {
        if (index >= tokens.lastIndex) return null
        index++
        return currentToken()
    }

    override fun previousToken(): String? {
        if (index <= 0) return null
        index--
        return currentToken()
    }

    override fun reset() {
        index = 0
    }

    override fun tokenCount(): Int = tokens.size

    override fun currentIndex(): Int = index

    override fun tokenAt(index: Int): String? = tokens.getOrNull(index)

    override fun rewind(steps: Int) {
        index = (index - steps).coerceAtLeast(0)
    }

    override fun intervalMillis(): Long {
        val safeWpm = wpm.coerceAtLeast(1)
        return 60_000L / safeWpm
    }

    override fun progressPercent(): Float {
        if (tokens.isEmpty()) return 0f
        return ((index + 1).toFloat() / tokens.size).coerceIn(0f, 1f)
    }

    override fun hasText(): Boolean = tokens.isNotEmpty()

    fun wordsPerMinute(): Int = wpm

    private fun normalize(text: String): String {
        if (text.isBlank()) return ""
        return text
            .replace("\r\n", "\n")
            .replace('\r', '\n')
            .replace(Regex("[ \\t]+"), " ")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }

    private fun splitWords(text: String): List<String> =
        text.split(Regex("\\s+")).filter { it.isNotEmpty() }

    private fun buildChunks(text: String, size: Int): List<String> {
        val words = splitWords(text)
        if (words.isEmpty()) return emptyList()
        if (size <= 1) return words

        val chunks = mutableListOf<String>()
        var i = 0
        while (i < words.size) {
            val end = (i + size).coerceAtMost(words.size)
            chunks.add(words.subList(i, end).joinToString(" "))
            i += size
        }
        return chunks
    }
}
