package com.heathen.hadeswatch.features.gateways

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.graphics.vector.ImageVector

enum class GatewayIcon(val label: String) {
    NAS("NAS"),
    MEDIA("Media"),
    MUSIC("Music"),
    DOWNLOADS("Downloads"),
    HOME_ASSISTANT("Home Assistant"),
    TERMINAL("Terminal"),
    DASHBOARD("Dashboard"),
    ARCHIVE("Archive"),
    CUSTOM("Custom"),
    ;

    companion object {
        fun fromName(name: String?): GatewayIcon =
            entries.find { it.name == name } ?: CUSTOM
    }
}

fun GatewayIcon.imageVector(): ImageVector = when (this) {
    GatewayIcon.NAS -> Icons.Default.Storage
    GatewayIcon.MEDIA -> Icons.Default.Videocam
    GatewayIcon.MUSIC -> Icons.Default.MusicNote
    GatewayIcon.DOWNLOADS -> Icons.Default.Download
    GatewayIcon.HOME_ASSISTANT -> Icons.Default.Home
    GatewayIcon.TERMINAL -> Icons.Default.Terminal
    GatewayIcon.DASHBOARD -> Icons.Default.Dashboard
    GatewayIcon.ARCHIVE -> Icons.Default.Archive
    GatewayIcon.CUSTOM -> Icons.Default.Cloud
}
