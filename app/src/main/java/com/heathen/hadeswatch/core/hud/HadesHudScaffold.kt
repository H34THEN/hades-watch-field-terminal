package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.heathen.hadeswatch.core.navigation.HadesDestination

@Composable
fun HadesHudScaffold(
    navController: NavHostController,
    currentRoute: String?,
    currentTabRoute: String,
    dockItems: List<HadesDestination>,
    k0ReaderEnabled: Boolean,
    gatewaysEnabled: Boolean,
    signalReaderEnabled: Boolean,
    fieldNotesEnabled: Boolean,
    aresEnabled: Boolean,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var showToolDrawer by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
        ) {
            content()
        }

        HudToolDrawerSheet(
            visible = showToolDrawer,
            onDismiss = { showToolDrawer = false },
            onNavigate = onNavigate,
            k0ReaderEnabled = k0ReaderEnabled,
            gatewaysEnabled = gatewaysEnabled,
            signalReaderEnabled = signalReaderEnabled,
            fieldNotesEnabled = fieldNotesEnabled,
            aresEnabled = aresEnabled,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp),
        ) {
            // Command rail: open tools drawer from Web tab
            if (currentTabRoute == HadesDestination.WebHub.tabRoute) {
                HudCommandFab(
                    onClick = { showToolDrawer = true },
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 80.dp),
                )
            }

            HudBottomDock(
                currentTabRoute = currentTabRoute,
                items = dockItems,
                onTabSelected = { destination ->
                    val target = navigationTargetFor(destination)
                    navController.navigate(target) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter),
            )
        }
    }
}

fun tabRouteFromNavRoute(route: String?): String = resolveTabRoute(route ?: HadesDestination.WebHub.tabRoute)
