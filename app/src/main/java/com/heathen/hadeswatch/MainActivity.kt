package com.heathen.hadeswatch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.HadesNavGraph
import com.heathen.hadeswatch.core.navigation.routeForBottomNav
import com.heathen.hadeswatch.core.theme.HadesWatchTheme
import com.heathen.hadeswatch.core.ui.HadesScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as HadesWatchApp
        val deepLinkRoute = intent?.let { resolveDeepLink(it) }

        setContent {
            val reducedMotion by app.settingsRepository.reducedMotion.collectAsState(initial = false)
            val highContrast by app.settingsRepository.highContrast.collectAsState(initial = false)
            val largeText by app.settingsRepository.largeText.collectAsState(initial = false)

            HadesWatchTheme(highContrast = highContrast, largeText = largeText) {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
                val currentRoute = currentBackStackEntry?.destination?.route

                if (deepLinkRoute != null && savedInstanceState == null) {
                    androidx.compose.runtime.LaunchedEffect(deepLinkRoute) {
                        navController.navigate(deepLinkRoute) {
                            launchSingleTop = true
                        }
                    }
                }

                HadesScaffold(
                    currentRoute = routeForBottomNav(currentRoute),
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(HadesDestination.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    bottomNavItems = HadesDestination.bottomNavItems,
                ) { modifier ->
                    HadesNavGraph(
                        navController = navController,
                        settingsRepository = app.settingsRepository,
                        sessionManager = app.sessionManager,
                    gatewayRepository = app.gatewayRepository,
                    signalSnippetRepository = app.signalSnippetRepository,
                )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveDeepLink(intent)?.let { route ->
            // Deep links on warm start handled on next composition via intent update
        }
    }

    private fun resolveDeepLink(intent: Intent): String? {
        val data = intent.data ?: return null
        if (data.scheme != "hadeswatch") return null
        val path = data.host ?: data.path?.removePrefix("/") ?: return null
        return HadesDestination.deepLinkRoute(path)
    }
}
