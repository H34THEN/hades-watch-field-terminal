package com.heathen.hadeswatch.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun HadesBottomNav(
    currentRoute: String,
    items: List<HadesDestination>,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { destination ->
            val selected = currentRoute == destination.tabRoute ||
                (destination.tabRoute == "web" && currentRoute.startsWith("web"))
            NavigationBarItem(
                selected = selected,
                onClick = {
                    when (destination) {
                        HadesDestination.WebHub -> onNavigate(HadesDestination.webHubRoute())
                        else -> onNavigate(destination.route)
                    }
                },
                icon = {
                    destination.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = destination.label,
                        )
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
                    indicatorColor = TerminalGreen.copy(alpha = 0.12f),
                ),
            )
        }
    }
}
