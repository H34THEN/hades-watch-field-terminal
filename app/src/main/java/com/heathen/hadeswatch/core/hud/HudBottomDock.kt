package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.theme.PanelDark
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun HudBottomDock(
    currentTabRoute: String,
    dockDestinations: List<HadesDestination>,
    onTabSelected: (HadesDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .heightIn(min = 64.dp),
        containerColor = PanelDark.copy(alpha = 0.96f),
    ) {
        for (destination in dockDestinations.filterNotNull()) {
            val tabRoute = destination.tabRoute
            val selected = if (tabRoute == HadesDestination.WebHub.tabRoute) {
                currentTabRoute == "web" || currentTabRoute.startsWith("web")
            } else {
                currentTabRoute == tabRoute
            }
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(destination) },
                icon = {
                    destination.icon?.let { icon ->
                        Icon(imageVector = icon, contentDescription = destination.label)
                    }
                },
                label = {
                    Text(
                        text = destination.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TerminalGreen,
                    selectedTextColor = TerminalGreen,
                    indicatorColor = TerminalGreen.copy(alpha = 0.15f),
                    unselectedIconColor = TerminalGreen.copy(alpha = 0.55f),
                    unselectedTextColor = TerminalGreen.copy(alpha = 0.55f),
                ),
            )
        }
    }
}

fun isTabSelected(currentTabRoute: String, destination: HadesDestination): Boolean {
    val tabRoute = destination.tabRoute
    return if (tabRoute == HadesDestination.WebHub.tabRoute) {
        currentTabRoute == "web" || currentTabRoute.startsWith("web")
    } else {
        currentTabRoute == tabRoute
    }
}

fun resolveTabRoute(route: String): String =
    when (destinationForRoute(route)) {
        HadesDestination.WebHub -> HadesDestination.WebHub.tabRoute
        HadesDestination.Tools -> HadesDestination.Tools.tabRoute
        HadesDestination.Reader -> HadesDestination.Reader.tabRoute
        HadesDestination.Settings -> HadesDestination.Settings.tabRoute
        else -> route.substringBefore("?")
    }

fun destinationForRoute(route: String?): HadesDestination? = when {
    route == null -> null
    route.startsWith("web") -> HadesDestination.WebHub
    route == HadesDestination.Reader.route || route == HadesDestination.K0Reader.route -> HadesDestination.Reader
    route == HadesDestination.Settings.route ||
        route.startsWith("settings/") -> HadesDestination.Settings
    route == HadesDestination.Tools.route || route.startsWith("tools/") -> HadesDestination.Tools
    else -> null
}

fun navigationTargetFor(destination: HadesDestination): String = when (destination) {
    HadesDestination.WebHub -> HadesDestination.webHubRoute()
    else -> destination.route
}
