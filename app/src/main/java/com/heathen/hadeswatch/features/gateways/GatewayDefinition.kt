package com.heathen.hadeswatch.features.gateways

import java.util.UUID

data class GatewayDefinition(
    val id: String = UUID.randomUUID().toString(),
    val displayName: String,
    val url: String,
    val iconKey: String = GatewayIcon.CUSTOM.name,
    val customIconUri: String? = null,
    val category: String = "",
    val note: String = "",
    val launchMode: GatewayLaunchMode = GatewayLaunchMode.EXTERNAL_BROWSER,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastOpenedAt: Long? = null,
    val sortOrder: Int = 0,
) {
    val icon: GatewayIcon get() = GatewayIcon.fromName(iconKey)

    fun withOpenedNow(): GatewayDefinition = copy(lastOpenedAt = System.currentTimeMillis())
}
