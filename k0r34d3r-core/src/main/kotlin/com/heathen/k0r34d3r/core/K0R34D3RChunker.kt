package com.heathen.k0r34d3r.core

/**
 * RSVP chunk building ported from K0R34D3R [TextProcessingService.buildRsvpChunks].
 *
 * Chunk size 1–3 maps to fixed word counts. Size 4+ uses phrase-style chunking
 * (punctuation boundaries, max 8 words per chunk).
 */
object K0R34D3RChunker {

    fun buildTokens(text: String, chunkSize: Int): List<String> {
        val words = K0R34D3RTokenizer.splitIntoWords(text)
        if (words.isEmpty()) return emptyList()

        return when {
            chunkSize <= 1 -> words
            chunkSize == 2 -> chunkByWordCount(words, 2)
            chunkSize == 3 -> chunkByWordCount(words, 3)
            else -> chunkByPhrase(words)
        }
    }

    fun wordIndexForChunkIndex(chunks: List<String>, chunkIndex: Int): Int {
        if (chunks.isEmpty() || chunkIndex <= 0) return 0
        var count = 0
        val limit = chunkIndex.coerceIn(0, chunks.size)
        for (i in 0 until limit) {
            count += K0R34D3RTokenizer.splitIntoWords(chunks[i]).size
        }
        return count
    }

    fun chunkIndexForWordIndex(chunks: List<String>, wordIndex: Int): Int {
        if (chunks.isEmpty() || wordIndex <= 0) return 0
        var seen = 0
        for (i in chunks.indices) {
            seen += K0R34D3RTokenizer.splitIntoWords(chunks[i]).size
            if (seen > wordIndex) return i
        }
        return chunks.lastIndex
    }

    fun optimalRecognitionPoint(chunk: String): Int {
        val word = chunk.trim()
        if (word.isEmpty()) return 0
        if (word.length <= 1) return 0
        if (word.length <= 5) return 1
        return (word.length * 0.37).toInt().coerceIn(1, word.length - 1)
    }

    private fun chunkByWordCount(words: List<String>, size: Int): List<String> {
        val chunks = mutableListOf<String>()
        var i = 0
        while (i < words.size) {
            val end = (i + size).coerceAtMost(words.size)
            chunks.add(words.subList(i, end).joinToString(" "))
            i += size
        }
        return chunks
    }

    private fun chunkByPhrase(words: List<String>): List<String> {
        val maxWords = 8
        val chunks = mutableListOf<String>()
        val buffer = mutableListOf<String>()

        for (word in words) {
            buffer.add(word)
            val endsPhrase = Regex("[.!?;:]$").containsMatchIn(word)
            if (endsPhrase || buffer.size >= maxWords) {
                chunks.add(buffer.joinToString(" "))
                buffer.clear()
            }
        }
        if (buffer.isNotEmpty()) {
            chunks.add(buffer.joinToString(" "))
        }
        return chunks
    }
}
