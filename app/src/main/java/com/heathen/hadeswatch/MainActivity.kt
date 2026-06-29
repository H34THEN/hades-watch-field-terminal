package com.heathen.hadeswatch

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.heathen.hadeswatch.core.hud.HadesHudScaffold
import com.heathen.hadeswatch.core.hud.WebShellController
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.HadesNavGraph
import com.heathen.hadeswatch.core.navigation.routeForBottomNav
import com.heathen.hadeswatch.core.theme.HadesWatchTheme

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
            val k0ReaderEnabled by app.settingsRepository.k0ReaderEnabled.collectAsState(initial = true)
            val gatewaysEnabled by app.settingsRepository.gatewaysEnabled.collectAsState(initial = true)
            val signalReaderEnabled by app.settingsRepository.signalReaderEnabled.collectAsState(initial = true)
            val aresEnabled by app.settingsRepository.aresEnabled.collectAsState(initial = true)
            val fieldNotesEnabled by app.settingsRepository.fieldNotesEnabled.collectAsState(initial = true)

            HadesWatchTheme(highContrast = highContrast, largeText = largeText) {
                val navController = rememberNavController()
                val webShellController = remember { WebShellController() }
                val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
                val currentRoute = currentBackStackEntry?.destination?.route

                if (deepLinkRoute != null && savedInstanceState == null) {
                    androidx.compose.runtime.LaunchedEffect(deepLinkRoute) {
                        navController.navigate(deepLinkRoute) {
                            launchSingleTop = true
                        }
                    }
                }

                HadesHudScaffold(
                    navController = navController,
                    settingsRepository = app.settingsRepository,
                    currentRoute = currentRoute,
                    currentTabRoute = routeForBottomNav(currentRoute),
                    webShellController = webShellController,
                    k0ReaderEnabled = k0ReaderEnabled,
                    gatewaysEnabled = gatewaysEnabled,
                    signalReaderEnabled = signalReaderEnabled,
                    fieldNotesEnabled = fieldNotesEnabled,
                    aresEnabled = aresEnabled,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    },
                ) {
                    HadesNavGraph(
                        navController = navController,
                        settingsRepository = app.settingsRepository,
                        sessionManager = app.sessionManager,
                        gatewayRepository = app.gatewayRepository,
                        signalSnippetRepository = app.signalSnippetRepository,
                        webShellController = webShellController,
                        startDestination = HadesDestination.webHubRoute(),
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun resolveDeepLink(intent: Intent): String? {
        val data = intent.data ?: return null
        if (data.scheme != "hadeswatch") return null
        val path = data.host ?: data.path?.removePrefix("/") ?: return null
        return HadesDestination.deepLinkRoute(path)
    }
}
