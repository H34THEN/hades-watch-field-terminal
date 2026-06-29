package com.heathen.hadeswatch.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HadesDestination(
    val route: String,
    val label: String,
    val icon: ImageVector? = null,
    val tabRoute: String = route,
) {
    data object Home : HadesDestination("home", "Home", Icons.Default.Home)
    data object WebHub : HadesDestination("web?url={url}", "Web", Icons.Default.Language, tabRoute = "web")
    data object Tools : HadesDestination("tools", "Tools", Icons.Default.Build)
    data object Reader : HadesDestination("reader", "Reader", Icons.AutoMirrored.Filled.MenuBook)
    data object Settings : HadesDestination("settings", "Settings", Icons.Default.Settings)

    // Legacy web sub-routes (deep links / internal navigation)
    data object Mmo : HadesDestination("mmo", "MMO")
    data object DeadDrops : HadesDestination("dead_drops", "Dead Drops")
    data object Forums : HadesDestination("forums", "Forums")
    data object Profile : HadesDestination("profile", "Profile")
    data object Notifications : HadesDestination("notifications", "Alerts")

    data object WebShell : HadesDestination("web/{url}", "Web")
    data object K0Reader : HadesDestination("tools/k0reader", "k0R34DER")
    data object Ares : HadesDestination("tools/ares", "4R3S")
    data object FieldNotes : HadesDestination("tools/fieldnotes", "Field Notes")
    data object UnderworldGateways : HadesDestination("tools/gateways", "Gateways")
    data object GatewayEditorNew : HadesDestination("tools/gateways/edit/new", "Add Gateway")
    data object GatewayEditor : HadesDestination("tools/gateways/edit/{gatewayId}", "Edit Gateway")
    data object GatewayViewer : HadesDestination("tools/gateways/view/{gatewayId}", "Gateway Viewer")
    data object SignalReader : HadesDestination("tools/signalreader", "Signal Reader")
    data object SignalSnippetEditorNew : HadesDestination("tools/signalreader/edit/new", "Add Signal")
    data object SignalSnippetEditor : HadesDestination("tools/signalreader/edit/{snippetId}", "Edit Signal")
    data object SignalSnippetDetail : HadesDestination("tools/signalreader/detail/{snippetId}", "Signal Detail")
    data object PrivacySafety : HadesDestination("settings/privacy", "Privacy & Safety")
    data object LocalToolData : HadesDestination("settings/local-tool-data", "Local Tool Data")
    data object FutureApiStatus : HadesDestination("settings/future-api", "Future API Status")

    companion object {
        val bottomNavItems = listOf(Home, WebHub, Tools, Reader, Settings)

        fun webHubRoute(url: String = WebRoutes.DASHBOARD): String =
            "web?url=${java.net.URLEncoder.encode(url, Charsets.UTF_8.name())}"

        fun webRoute(url: String): String = "web/${java.net.URLEncoder.encode(url, Charsets.UTF_8.name())}"

        fun gatewayEditorRoute(gatewayId: String? = null): String =
            if (gatewayId.isNullOrBlank()) GatewayEditorNew.route else "tools/gateways/edit/$gatewayId"

        fun gatewayViewerRoute(gatewayId: String): String = "tools/gateways/view/$gatewayId"

        fun signalSnippetEditorRoute(snippetId: String? = null): String =
            if (snippetId.isNullOrBlank()) SignalSnippetEditorNew.route else "tools/signalreader/edit/$snippetId"

        fun signalSnippetDetailRoute(snippetId: String): String = "tools/signalreader/detail/$snippetId"

        fun deepLinkRoute(schemePath: String): String? = when (schemePath.lowercase()) {
            "dashboard" -> webHubRoute(WebRoutes.DASHBOARD)
            "mmo" -> webHubRoute(WebRoutes.MMO)
            "dead-drops", "deaddrops" -> webHubRoute(WebRoutes.DEAD_DROPS)
            "forums" -> webHubRoute(WebRoutes.FORUMS)
            "profile" -> webHubRoute(WebRoutes.PROFILE_DOSSIER)
            "tools" -> Tools.route
            "reader", "k0reader" -> Reader.route
            "notifications" -> webHubRoute(WebRoutes.NOTIFICATIONS)
            "settings" -> Settings.route
            "login" -> webHubRoute(WebRoutes.LOGIN)
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

data class WebRouteOption(
    val label: String,
    val url: String,
    val iconKey: com.heathen.hadeswatch.core.ui.ToolIconKey,
)

val primaryWebRoutes = listOf(
    WebRouteOption("Dashboard", WebRoutes.DASHBOARD, com.heathen.hadeswatch.core.ui.ToolIconKey.DASHBOARD),
    WebRouteOption("MMO", WebRoutes.MMO, com.heathen.hadeswatch.core.ui.ToolIconKey.MMO),
    WebRouteOption("Dead Drops", WebRoutes.DEAD_DROPS, com.heathen.hadeswatch.core.ui.ToolIconKey.DEAD_DROP),
    WebRouteOption("Forums", WebRoutes.FORUMS, com.heathen.hadeswatch.core.ui.ToolIconKey.FORUMS),
    WebRouteOption("Profile", WebRoutes.PROFILE_DOSSIER, com.heathen.hadeswatch.core.ui.ToolIconKey.PROFILE),
    WebRouteOption("Notifications", WebRoutes.NOTIFICATIONS, com.heathen.hadeswatch.core.ui.ToolIconKey.NOTIFICATIONS),
)
