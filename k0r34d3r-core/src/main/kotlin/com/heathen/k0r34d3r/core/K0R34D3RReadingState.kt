package com.heathen.k0r34d3r.core

data class K0R34D3RReadingState(
    val tokens: List<String>,
    val currentIndex: Int,
    val config: K0R34D3RReaderConfig,
)
