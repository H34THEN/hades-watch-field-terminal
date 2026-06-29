package com.heathen.hadeswatch.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.ui.HadesTerminalCard

data class QuickLaunchItem(val label: String, val url: String)

private val quickLaunchItems = listOf(
    QuickLaunchItem("Dashboard", WebRoutes.DASHBOARD),
    QuickLaunchItem("MMO Hub", WebRoutes.MMO),
    QuickLaunchItem("Daily Signals", WebRoutes.DAILY_SIGNALS),
    QuickLaunchItem("Dead Drops", WebRoutes.DEAD_DROPS),
    QuickLaunchItem("Faction Calls", WebRoutes.FACTION_CALLS),
    QuickLaunchItem("Forums", WebRoutes.FORUMS),
    QuickLaunchItem("Guilds", WebRoutes.GUILDS),
    QuickLaunchItem("Net Neighbors", WebRoutes.NET_NEIGHBORS),
    QuickLaunchItem("Profile Dossier", WebRoutes.PROFILE_DOSSIER),
    QuickLaunchItem("Avatar Builder", WebRoutes.AVATAR_BUILDER),
    QuickLaunchItem("Relic Zone", WebRoutes.RELIC_ZONE),
    QuickLaunchItem("Profile World", WebRoutes.PROFILE_WORLD),
    QuickLaunchItem("Archive", WebRoutes.ARCHIVE),
    QuickLaunchItem("Ghost in Tech", WebRoutes.GHOST_IN_TECH),
    QuickLaunchItem("State of Affairs", WebRoutes.STATE_OF_AFFAIRS),
    QuickLaunchItem("Notifications", WebRoutes.NOTIFICATIONS),
    QuickLaunchItem("Login", WebRoutes.LOGIN),
)

@Composable
fun HomeScreen(onNavigateToWeb: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Field Terminal",
                style = MaterialTheme.typography.displayLarge,
                color = SignalCyan,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Text(
                text = "Underwatch uplink to hadeswatch.com",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        items(quickLaunchItems) { item ->
            HadesTerminalCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToWeb(HadesDestination.webRoute(item.url)) },
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
