package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HadesScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    bottomNavItems: List<com.heathen.hadeswatch.core.navigation.HadesDestination>,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            HadesBottomNav(
                currentRoute = currentRoute,
                items = bottomNavItems,
                onNavigate = onNavigate,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            content(Modifier.fillMaxSize())
        }
    }
}
