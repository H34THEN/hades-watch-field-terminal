package com.heathen.hadeswatch.features.k0reader

/**
 * In-memory transfer for passing text from other tools (e.g. Signal Reader) to k0R34DER
 * without putting large bodies in navigation route arguments.
 *
 * Transfer state does not survive app process death — it is intentionally temporary.
 */
object ReaderTransferRepository {

    data class PendingTransfer(
        val text: String,
        val sourceTitle: String = "",
    )

    private var pending: PendingTransfer? = null

    fun setPending(text: String, sourceTitle: String = "") {
        pending = PendingTransfer(text = text, sourceTitle = sourceTitle.trim())
    }

    /** @deprecated Use [setPending] */
    fun setPendingText(text: String) = setPending(text)

    fun consumePending(): PendingTransfer? {
        val transfer = pending
        pending = null
        return transfer
    }

    /** @deprecated Use [consumePending] */
    fun consumePendingText(): String? = consumePending()?.text

    fun peekPending(): PendingTransfer? = pending

    fun clearPending() {
        pending = null
    }
}
