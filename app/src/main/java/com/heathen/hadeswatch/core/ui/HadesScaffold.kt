package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination

@Composable
fun HadesScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    bottomNavItems: List<HadesDestination>,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { destination ->
                    val selected = currentRoute == destination.route ||
                        (destination.route == "notifications" && currentRoute.contains("notifications"))
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onNavigate(destination.route) },
                        icon = {
                            destination.icon?.let {
                                Icon(imageVector = it, contentDescription = destination.label)
                            }
                        },
                        label = {
                            Text(
                                text = destination.label,
                                maxLines = 1,
                            )
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 0.dp),
        ) {
            content(Modifier.fillMaxSize())
        }
    }
}
