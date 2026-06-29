package com.heathen.hadeswatch.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HadesDestination(
    val route: String,
    val label: String,
    val icon: ImageVector? = null,
) {
    data object Home : HadesDestination("home", "Home", Icons.Default.Home)
    data object Mmo : HadesDestination("mmo", "MMO", Icons.Default.Visibility)
    data object DeadDrops : HadesDestination("dead_drops", "Dead Drops")
    data object Forums : HadesDestination("forums", "Forums", Icons.Default.Chat)
    data object Profile : HadesDestination("profile", "Profile", Icons.Default.Person)
    data object Tools : HadesDestination("tools", "Tools", Icons.Default.Build)
    data object Notifications : HadesDestination("notifications", "Alerts", Icons.Default.Notifications)
    data object Settings : HadesDestination("settings", "Settings", Icons.Default.Settings)

    data object WebShell : HadesDestination("web/{url}", "Web")
    data object K0Reader : HadesDestination("tools/k0reader", "k0R34DER")
    data object Ares : HadesDestination("tools/ares", "4R3S")
    data object FieldNotes : HadesDestination("tools/fieldnotes", "Field Notes")
    data object PrivacySafety : HadesDestination("settings/privacy", "Privacy & Safety")

    companion object {
        val bottomNavItems = listOf(Home, Mmo, DeadDrops, Forums, Profile, Tools, Notifications, Settings)

        fun webRoute(url: String): String = "web/${java.net.URLEncoder.encode(url, Charsets.UTF_8.name())}"

        fun deepLinkRoute(schemePath: String): String? = when (schemePath.lowercase()) {
            "dashboard" -> webRoute(WebRoutes.DASHBOARD)
            "mmo" -> Mmo.route
            "dead-drops", "deaddrops" -> DeadDrops.route
            "forums" -> Forums.route
            "profile" -> Profile.route
            "tools" -> Tools.route
            "notifications" -> Notifications.route
            "settings" -> Settings.route
            "login" -> webRoute(WebRoutes.LOGIN)
            else -> null
        }
    }
}

object WebRoutes {
    const val BASE = "https://hadeswatch.com"
    const val DASHBOARD = "$BASE/dashboard"
    const val MMO = "$BASE/mmo"
    const val DAILY_SIGNALS = "$BASE/daily-signals"
    const val DEAD_DROPS = "$BASE/dead-drops"
    const val FACTION_CALLS = "$BASE/faction-calls"
    const val FORUMS = "$BASE/community/forums"
    const val GUILDS = "$BASE/guilds"
    const val NET_NEIGHBORS = "$BASE/net-neighbors"
    const val PROFILE_DOSSIER = "$BASE/profile/dossier"
    const val AVATAR_BUILDER = "$BASE/avatar-builder"
    const val RELIC_ZONE = "$BASE/relic-zone"
    const val PROFILE_WORLD = "$BASE/profile-world"
    const val ARCHIVE = "$BASE/archive"
    const val GHOST_IN_TECH = "$BASE/ghost-in-tech"
    const val STATE_OF_AFFAIRS = "$BASE/state-of-affairs"
    const val NOTIFICATIONS = "$BASE/notifications"
    const val LOGIN = "$BASE/login"
}
