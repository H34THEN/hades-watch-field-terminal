package com.heathen.hadeswatch.features.signalreader

import java.util.UUID

data class SignalSnippet(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val body: String,
    val sourceLabel: String = "",
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val sortOrder: Int = 0,
)
