package com.heathen.k0r34d3r.core.epub

data class EpubParseResult(
    val title: String,
    val plainText: String,
    val chapterCount: Int,
)

class EpubImportException(message: String) : Exception(message)
