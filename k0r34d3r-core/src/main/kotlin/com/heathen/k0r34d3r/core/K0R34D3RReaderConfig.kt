package com.heathen.k0r34d3r.core

data class K0R34D3RReaderConfig(
    val wordsPerMinute: Int = 300,
    val chunkSize: Int = 1,
    val fontScale: Float = 1.0f,
    val preservePunctuation: Boolean = true,
    val reduceMotion: Boolean = true,
) {
    init {
        require(wordsPerMinute in MIN_WPM..MAX_WPM) {
            "wordsPerMinute must be between $MIN_WPM and $MAX_WPM"
        }
        require(chunkSize in 1..MAX_CHUNK_SIZE) {
            "chunkSize must be between 1 and $MAX_CHUNK_SIZE"
        }
        require(fontScale in 0.5f..3.0f) { "fontScale must be between 0.5 and 3.0" }
    }

    companion object {
        const val MIN_WPM = 100
        const val MAX_WPM = 1200
        const val MAX_CHUNK_SIZE = 8
        const val PHRASE_CHUNK_SIZE = 4
    }
}
