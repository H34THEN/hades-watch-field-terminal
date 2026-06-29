package com.heathen.hadeswatch.features.gateways

import java.util.UUID

data class GatewayDefinition(
    val id: String = UUID.randomUUID().toString(),
    val displayName: String,
    val url: String,
    val icon: GatewayIcon = GatewayIcon.CUSTOM,
    val category: String = "",
    val note: String = "",
)
