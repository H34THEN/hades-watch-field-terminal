package com.heathen.hadeswatch.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToolIconKey {
    READER,
    SIGNAL,
    GATEWAY,
    NOTES,
    DEAD_DROP,
    ARES,
    ACCESSIBILITY,
    WEB,
    SETTINGS,
    WARNING,
    INFO,
    ARCHIVE,
    TERMINAL,
    HOME,
    DASHBOARD,
    MMO,
    FORUMS,
    PROFILE,
    NOTIFICATIONS,
}

object HadesIcons {
    fun forKey(key: ToolIconKey): ImageVector = when (key) {
        ToolIconKey.READER -> Icons.AutoMirrored.Filled.MenuBook
        ToolIconKey.SIGNAL -> Icons.Default.RssFeed
        ToolIconKey.GATEWAY -> Icons.Default.Storage
        ToolIconKey.NOTES -> Icons.AutoMirrored.Filled.NoteAdd
        ToolIconKey.DEAD_DROP -> Icons.Default.Archive
        ToolIconKey.ARES -> Icons.Default.Radar
        ToolIconKey.ACCESSIBILITY -> Icons.Default.Accessibility
        ToolIconKey.WEB -> Icons.Default.Language
        ToolIconKey.SETTINGS -> Icons.Default.Settings
        ToolIconKey.WARNING -> Icons.Default.Warning
        ToolIconKey.INFO -> Icons.Default.Info
        ToolIconKey.ARCHIVE -> Icons.Default.Archive
        ToolIconKey.TERMINAL -> Icons.Default.Terminal
        ToolIconKey.HOME -> Icons.Default.Home
        ToolIconKey.DASHBOARD -> Icons.Default.Dashboard
        ToolIconKey.MMO -> Icons.Default.Visibility
        ToolIconKey.FORUMS -> Icons.Default.Forum
        ToolIconKey.PROFILE -> Icons.Default.Person
        ToolIconKey.NOTIFICATIONS -> Icons.Default.Notifications
    }

    fun forToolIconKey(iconKey: String): ImageVector = when (iconKey.lowercase()) {
        "k0reader" -> forKey(ToolIconKey.READER)
        "signal_reader" -> forKey(ToolIconKey.SIGNAL)
        "gateways" -> forKey(ToolIconKey.GATEWAY)
        "fieldnotes" -> forKey(ToolIconKey.NOTES)
        "dead_drops" -> forKey(ToolIconKey.DEAD_DROP)
        "ares" -> forKey(ToolIconKey.ARES)
        "accessibility" -> forKey(ToolIconKey.ACCESSIBILITY)
        else -> forKey(ToolIconKey.TERMINAL)
    }
}

@Composable
fun HadesIcon(
    key: ToolIconKey,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = HadesIcons.forKey(key),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
