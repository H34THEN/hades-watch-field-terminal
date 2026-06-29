package com.heathen.hadeswatch.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.heathen.hadeswatch.core.navigation.WebRoutes.BASE
import com.heathen.hadeswatch.core.security.SessionManager
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.features.ares.AresModuleScreen
import com.heathen.hadeswatch.features.fieldnotes.FieldNotesScreen
import com.heathen.hadeswatch.features.gateways.GatewayEditorScreen
import com.heathen.hadeswatch.features.gateways.GatewayRepository
import com.heathen.hadeswatch.features.gateways.UnderworldGatewaysScreen
import com.heathen.hadeswatch.features.gateways.viewer.GenericGatewayViewerScreen
import com.heathen.hadeswatch.features.home.HomeScreen
import com.heathen.hadeswatch.features.k0reader.K0ReaderScreen
import com.heathen.hadeswatch.features.notifications.NotificationsScreen
import com.heathen.hadeswatch.features.settings.FutureApiStatusScreen
import com.heathen.hadeswatch.features.settings.LocalToolDataScreen
import com.heathen.hadeswatch.features.settings.PrivacySafetyScreen
import com.heathen.hadeswatch.features.settings.SettingsScreen
import com.heathen.hadeswatch.features.signalreader.SignalReaderDetailScreen
import com.heathen.hadeswatch.features.signalreader.SignalReaderScreen
import com.heathen.hadeswatch.features.signalreader.SignalSnippetEditorScreen
import com.heathen.hadeswatch.features.signalreader.SignalSnippetRepository
import com.heathen.hadeswatch.features.tools.ToolsHubScreen
import com.heathen.hadeswatch.features.webshell.WebShellScreen
import java.net.URLDecoder

@Composable
fun HadesNavGraph(
    navController: NavHostController,
    settingsRepository: AppSettingsRepository,
    sessionManager: SessionManager,
    gatewayRepository: GatewayRepository,
    signalSnippetRepository: SignalSnippetRepository,
    startDestination: String = HadesDestination.Home.route,
) {
    val reducedMotion by settingsRepository.reducedMotion.collectAsState(initial = false)
    val highContrast by settingsRepository.highContrast.collectAsState(initial = false)
    val largeText by settingsRepository.largeText.collectAsState(initial = false)
    val openExternal by settingsRepository.openExternalInBrowser.collectAsState(initial = true)
    val k0ReaderEnabled by settingsRepository.k0ReaderEnabled.collectAsState(initial = true)
    val gatewaysEnabled by settingsRepository.gatewaysEnabled.collectAsState(initial = true)
    val signalReaderEnabled by settingsRepository.signalReaderEnabled.collectAsState(initial = true)
    val aresEnabled by settingsRepository.aresEnabled.collectAsState(initial = true)
    val fieldNotesEnabled by settingsRepository.fieldNotesEnabled.collectAsState(initial = true)
    val k0ReaderUseSdk by settingsRepository.k0ReaderUseSdkAdapter.collectAsState(initial = true)
    val gateways by gatewayRepository.gateways.collectAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(HadesDestination.Home.route) {
            HomeScreen(onNavigateToWeb = { navController.navigate(it) })
        }
        composable(HadesDestination.Mmo.route) {
            WebShellScreen(url = WebRoutes.MMO, openExternalInBrowser = openExternal)
        }
        composable(HadesDestination.DeadDrops.route) {
            WebShellScreen(url = WebRoutes.DEAD_DROPS, openExternalInBrowser = openExternal)
        }
        composable(HadesDestination.Forums.route) {
            WebShellScreen(url = WebRoutes.FORUMS, openExternalInBrowser = openExternal)
        }
        composable(HadesDestination.Profile.route) {
            WebShellScreen(url = WebRoutes.PROFILE_DOSSIER, openExternalInBrowser = openExternal)
        }
        composable(HadesDestination.Tools.route) {
            ToolsHubScreen(
                onNavigate = { navController.navigate(it) },
                k0ReaderEnabled = k0ReaderEnabled,
                gatewaysEnabled = gatewaysEnabled,
                signalReaderEnabled = signalReaderEnabled,
                aresEnabled = aresEnabled,
                fieldNotesEnabled = fieldNotesEnabled,
                compactMode = !largeText,
            )
        }
        composable(HadesDestination.Notifications.route) {
            NotificationsScreen(openExternalInBrowser = openExternal)
        }
        composable(HadesDestination.Settings.route) {
            SettingsScreen(
                settingsRepository = settingsRepository,
                sessionManager = sessionManager,
                reducedMotion = reducedMotion,
                highContrast = highContrast,
                largeText = largeText,
                openExternalInBrowser = openExternal,
                k0ReaderEnabled = k0ReaderEnabled,
                k0ReaderUseSdk = k0ReaderUseSdk,
                gatewaysEnabled = gatewaysEnabled,
                signalReaderEnabled = signalReaderEnabled,
                aresEnabled = aresEnabled,
                fieldNotesEnabled = fieldNotesEnabled,
                onNavigate = { navController.navigate(it) },
            )
        }
        composable(HadesDestination.PrivacySafety.route) {
            PrivacySafetyScreen()
        }
        composable(HadesDestination.LocalToolData.route) {
            LocalToolDataScreen(
                settingsRepository = settingsRepository,
                gatewayRepository = gatewayRepository,
                signalSnippetRepository = signalSnippetRepository,
            )
        }
        composable(HadesDestination.FutureApiStatus.route) {
            FutureApiStatusScreen()
        }
        composable(HadesDestination.K0Reader.route) {
            K0ReaderScreen(
                settingsRepository = settingsRepository,
                reducedMotion = reducedMotion,
            )
        }
        composable(HadesDestination.UnderworldGateways.route) {
            UnderworldGatewaysScreen(
                gatewayRepository = gatewayRepository,
                onAddGateway = { navController.navigate(HadesDestination.gatewayEditorRoute()) },
                onEditGateway = { id -> navController.navigate(HadesDestination.gatewayEditorRoute(id)) },
                onOpenInAppViewer = { gateway ->
                    navController.navigate(HadesDestination.gatewayViewerRoute(gateway.id))
                },
            )
        }
        composable(
            route = HadesDestination.GatewayViewer.route,
            arguments = listOf(navArgument("gatewayId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val gatewayId = backStackEntry.arguments?.getString("gatewayId")
            val gateway = gateways.find { it.id == gatewayId }
            if (gateway != null) {
                GenericGatewayViewerScreen(
                    gateway = gateway,
                    onClose = { navController.popBackStack() },
                )
            }
        }
        composable(HadesDestination.GatewayEditorNew.route) {
            GatewayEditorScreen(
                gatewayRepository = gatewayRepository,
                gatewayId = null,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
            )
        }
        composable(
            route = HadesDestination.GatewayEditor.route,
            arguments = listOf(navArgument("gatewayId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val gatewayId = backStackEntry.arguments?.getString("gatewayId")
            GatewayEditorScreen(
                gatewayRepository = gatewayRepository,
                gatewayId = gatewayId,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
            )
        }
        composable(HadesDestination.SignalReader.route) {
            SignalReaderScreen(
                repository = signalSnippetRepository,
                onAddSnippet = { navController.navigate(HadesDestination.signalSnippetEditorRoute()) },
                onOpenSnippet = { id -> navController.navigate(HadesDestination.signalSnippetDetailRoute(id)) },
            )
        }
        composable(HadesDestination.SignalSnippetEditorNew.route) {
            SignalSnippetEditorScreen(
                repository = signalSnippetRepository,
                snippetId = null,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
            )
        }
        composable(
            route = HadesDestination.SignalSnippetEditor.route,
            arguments = listOf(navArgument("snippetId") { type = NavType.StringType }),
        ) { backStackEntry ->
            SignalSnippetEditorScreen(
                repository = signalSnippetRepository,
                snippetId = backStackEntry.arguments?.getString("snippetId"),
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
            )
        }
        composable(
            route = HadesDestination.SignalSnippetDetail.route,
            arguments = listOf(navArgument("snippetId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val snippetId = backStackEntry.arguments?.getString("snippetId") ?: return@composable
            SignalReaderDetailScreen(
                repository = signalSnippetRepository,
                snippetId = snippetId,
                onEdit = { navController.navigate(HadesDestination.signalSnippetEditorRoute(snippetId)) },
                onReadInK0Reader = {
                    navController.navigate(HadesDestination.K0Reader.route) {
                        popUpTo(HadesDestination.Tools.route) { saveState = true }
                        launchSingleTop = true
                    }
                },
                onDeleted = { navController.popBackStack() },
            )
        }
        composable(HadesDestination.Ares.route) {
            AresModuleScreen()
        }
        composable(HadesDestination.FieldNotes.route) {
            FieldNotesScreen(settingsRepository = settingsRepository)
        }
        composable(
            route = HadesDestination.WebShell.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType }),
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("url").orEmpty()
            val url = URLDecoder.decode(encoded, Charsets.UTF_8.name())
            WebShellScreen(
                url = url.ifBlank { BASE },
                openExternalInBrowser = openExternal,
            )
        }
    }
}

fun routeForBottomNav(route: String?): String = when {
    route?.startsWith("web/") == true -> HadesDestination.Home.route
    route == HadesDestination.K0Reader.route -> HadesDestination.Tools.route
    route == HadesDestination.Ares.route -> HadesDestination.Tools.route
    route == HadesDestination.FieldNotes.route -> HadesDestination.Tools.route
    route == HadesDestination.UnderworldGateways.route -> HadesDestination.Tools.route
    route == HadesDestination.SignalReader.route -> HadesDestination.Tools.route
    route?.startsWith("tools/gateways/") == true -> HadesDestination.Tools.route
    route?.startsWith("tools/signalreader/") == true -> HadesDestination.Tools.route
    route == HadesDestination.PrivacySafety.route -> HadesDestination.Settings.route
    route == HadesDestination.LocalToolData.route -> HadesDestination.Settings.route
    route == HadesDestination.FutureApiStatus.route -> HadesDestination.Settings.route
    else -> route ?: HadesDestination.Home.route
}
