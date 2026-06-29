package com.heathen.hadeswatch.features.k0reader

/**
 * In-memory transfer for passing text from other tools (e.g. Signal Reader) to k0R34DER
 * without putting large bodies in navigation route arguments.
 */
object ReaderTransferRepository {
    private var pendingText: String? = null

    fun setPendingText(text: String) {
        pendingText = text
    }

    fun consumePendingText(): String? {
        val text = pendingText
        pendingText = null
        return text
    }

    fun peekPendingText(): String? = pendingText
}
