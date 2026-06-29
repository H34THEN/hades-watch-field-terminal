package com.heathen.hadeswatch.core.hud

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.ToolIconKey
import kotlinx.coroutines.launch

@Composable
fun HadesHudScaffold(
    navController: NavHostController,
    settingsRepository: AppSettingsRepository,
    currentRoute: String?,
    currentTabRoute: String,
    webShellController: WebShellController,
    k0ReaderEnabled: Boolean,
    gatewaysEnabled: Boolean,
    signalReaderEnabled: Boolean,
    fieldNotesEnabled: Boolean,
    aresEnabled: Boolean,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val positionStore = remember { CommandHexPositionStore(context) }
    val hexState = remember { CommandHexState() }

    val fieldHexEnabled by settingsRepository.fieldHexEnabled.collectAsState(initial = true)
    val fieldHexSize by settingsRepository.fieldHexSize.collectAsState(initial = 1)
    val fieldHexOpacity by settingsRepository.fieldHexOpacity.collectAsState(initial = 1)
    val storedPosition by positionStore.position.collectAsState(initial = HexPosition.Default)

    var hexPosition by remember { mutableStateOf(HexPosition.Default) }
    var showRouteSheet by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(storedPosition) {
        if (!isDragging) {
            hexPosition = storedPosition
        }
    }

    val isWebScreen = currentTabRoute == HadesDestination.WebHub.tabRoute ||
        currentRoute?.startsWith("web") == true

    LaunchedEffect(currentRoute) {
        hexState.isMenuOpen = false
    }

    LaunchedEffect(webShellController) {
        webShellController.registerRouteSelector { showRouteSheet = true }
    }

    BackHandler(enabled = hexState.isMenuOpen || showRouteSheet) {
        if (showRouteSheet) {
            showRouteSheet = false
        } else {
            hexState.isMenuOpen = false
        }
    }

    HudRouteSelectorSheet(
        visible = showRouteSheet,
        currentUrl = webShellController.currentUrl,
        onDismiss = { showRouteSheet = false },
        onSelectRoute = { route ->
            if (isWebScreen) {
                webShellController.navigateToUrl(route.url)
            } else {
                onNavigate(HadesDestination.webHubRoute(route.url))
            }
            showRouteSheet = false
        },
    )

    val hexSize = CommandHexBounds.hexSizeDp(
        FieldHexSize.entries.getOrElse(fieldHexSize) { FieldHexSize.Medium },
    )
    val hexOpacity = CommandHexBounds.opacityValue(
        FieldHexOpacity.entries.getOrElse(fieldHexOpacity) { FieldHexOpacity.Medium },
    )

    val menuActions = buildCommandHexActions(
        isWebScreen = isWebScreen,
        webShellController = webShellController,
        k0ReaderEnabled = k0ReaderEnabled,
        gatewaysEnabled = gatewaysEnabled,
        signalReaderEnabled = signalReaderEnabled,
        fieldNotesEnabled = fieldNotesEnabled,
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val containerWidthPx = with(density) { maxWidth.toPx() }
        val containerHeightPx = with(density) { maxHeight.toPx() }
        val safePadding = WindowInsets.safeDrawing.asPaddingValues()
        val safeLeftPx = with(density) { safePadding.calculateLeftPadding(layoutDirection).toPx() }
        val safeTopPx = with(density) { safePadding.calculateTopPadding().toPx() }
        val safeRightPx = with(density) { safePadding.calculateRightPadding(layoutDirection).toPx() }
        val safeBottomPx = with(density) { safePadding.calculateBottomPadding().toPx() }
        val safeWidthPx = (containerWidthPx - safeLeftPx - safeRightPx).coerceAtLeast(1f)
        val safeHeightPx = (containerHeightPx - safeTopPx - safeBottomPx).coerceAtLeast(1f)

        val clampedPosition = CommandHexBounds.clamp(
            hexPosition,
            hexSize,
            safePadding,
            containerWidthPx,
            containerHeightPx,
            density.density,
        )
        LaunchedEffect(clampedPosition.xFraction, clampedPosition.yFraction, isDragging) {
            if (!isDragging && clampedPosition != hexPosition) {
                hexPosition = clampedPosition
            }
        }

        Box(
            modifier = if (isWebScreen) {
                Modifier.fillMaxSize()
            } else {
                Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            },
        ) {
            content()
        }

        if (fieldHexEnabled && !hexState.isHidden()) {
            val hexPx = with(density) { hexSize.toPx() }
            val centerX = safeLeftPx + clampedPosition.xFraction * safeWidthPx
            val centerY = safeTopPx + clampedPosition.yFraction * safeHeightPx

            CommandHex(
                position = clampedPosition,
                hexSize = hexSize,
                opacity = hexOpacity,
                isMenuOpen = hexState.isMenuOpen,
                isDragging = isDragging,
                onPositionChange = { updated ->
                    hexPosition = updated
                },
                onDragEnd = { final ->
                    hexPosition = final
                    scope.launch { positionStore.save(final) }
                },
                onTap = { hexState.isMenuOpen = !hexState.isMenuOpen },
                onLongPress = {
                    hexPosition = HexPosition.Default
                    scope.launch { positionStore.reset() }
                },
                onDragStateChange = { isDragging = it },
                safeLeftPx = safeLeftPx,
                safeTopPx = safeTopPx,
                safeWidthPx = safeWidthPx,
                safeHeightPx = safeHeightPx,
            )

            CommandHexMenu(
                visible = hexState.isMenuOpen,
                hexCenterX = centerX,
                hexCenterY = centerY,
                hexSizePx = hexPx,
                screenWidthPx = containerWidthPx,
                screenHeightPx = containerHeightPx,
                actions = menuActions,
                onDismiss = { hexState.isMenuOpen = false },
                onAction = { action ->
                    handleCommandHexAction(
                        action = action,
                        navController = navController,
                        webShellController = webShellController,
                        isWebScreen = isWebScreen,
                        onNavigate = onNavigate,
                        onOpenRouteSelector = { showRouteSheet = true },
                        onHideHex = { hexState.hideTemporarily(10) },
                        onResetHex = {
                            hexPosition = HexPosition.Default
                            scope.launch { positionStore.reset() }
                        },
                    )
                },
            )
        } else if (!fieldHexEnabled) {
            TextButton(
                onClick = { onNavigate(HadesDestination.Settings.route) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(8.dp),
            ) {
                Text("Settings", color = TerminalGreen)
            }
        }
    }
}

private fun buildCommandHexActions(
    isWebScreen: Boolean,
    webShellController: WebShellController,
    k0ReaderEnabled: Boolean,
    gatewaysEnabled: Boolean,
    signalReaderEnabled: Boolean,
    fieldNotesEnabled: Boolean,
): List<CommandHexAction> = buildList {
    add(CommandHexAction("web", "Web", CommandHexActionGroup.Navigation, ToolIconKey.WEB))
    add(CommandHexAction("tools", "Tools", CommandHexActionGroup.Navigation, ToolIconKey.TERMINAL))
    add(CommandHexAction("reader", "Reader", CommandHexActionGroup.Navigation, ToolIconKey.READER, k0ReaderEnabled))
    add(CommandHexAction("settings", "Settings", CommandHexActionGroup.Navigation, ToolIconKey.SETTINGS))

    if (isWebScreen && webShellController.isActive) {
        add(CommandHexAction("back", "Back", CommandHexActionGroup.WebControls, ToolIconKey.WEB, webShellController.canGoBack))
        add(CommandHexAction("forward", "Forward", CommandHexActionGroup.WebControls, ToolIconKey.WEB, webShellController.canGoForward))
        add(CommandHexAction("refresh", "Refresh", CommandHexActionGroup.WebControls, ToolIconKey.WEB))
        add(CommandHexAction("routes", "Route selector", CommandHexActionGroup.WebControls, ToolIconKey.DASHBOARD))
        add(CommandHexAction("dashboard", "Dashboard", CommandHexActionGroup.WebControls, ToolIconKey.DASHBOARD))
        add(CommandHexAction("login", "Login", CommandHexActionGroup.WebControls, ToolIconKey.WEB))
    }

    if (k0ReaderEnabled) {
        add(CommandHexAction("k0reader", "k0R34DER", CommandHexActionGroup.Tools, ToolIconKey.READER))
    }
    if (signalReaderEnabled) {
        add(CommandHexAction("signal", "Signal Reader", CommandHexActionGroup.Tools, ToolIconKey.SIGNAL))
    }
    if (gatewaysEnabled) {
        add(CommandHexAction("gateways", "Underworld Gateways", CommandHexActionGroup.Tools, ToolIconKey.GATEWAY))
    }
    if (fieldNotesEnabled) {
        add(CommandHexAction("notes", "Field Notes", CommandHexActionGroup.Tools, ToolIconKey.NOTES))
    }
    add(CommandHexAction("localdata", "Local Tool Data", CommandHexActionGroup.Tools, ToolIconKey.ARCHIVE))
    add(CommandHexAction("privacy", "Privacy & Safety", CommandHexActionGroup.Utility, ToolIconKey.SETTINGS))
    add(CommandHexAction("hide", "Hide Field Hex (10s)", CommandHexActionGroup.Utility, ToolIconKey.WEB))
    add(CommandHexAction("reset", "Reset hex position", CommandHexActionGroup.Utility, ToolIconKey.SETTINGS))
}

private fun handleCommandHexAction(
    action: CommandHexAction,
    navController: NavHostController,
    webShellController: WebShellController,
    isWebScreen: Boolean,
    onNavigate: (String) -> Unit,
    onOpenRouteSelector: () -> Unit,
    onHideHex: () -> Unit,
    onResetHex: () -> Unit,
) {
    when (action.id) {
        "web" -> onNavigate(HadesDestination.webHubRoute())
        "tools" -> onNavigate(HadesDestination.Tools.route)
        "reader" -> onNavigate(HadesDestination.Reader.route)
        "settings" -> onNavigate(HadesDestination.Settings.route)
        "back" -> webShellController.goBack()
        "forward" -> webShellController.goForward()
        "refresh" -> webShellController.reload()
        "routes" -> onOpenRouteSelector()
        "dashboard" -> {
            if (isWebScreen) webShellController.navigateToUrl(WebRoutes.DASHBOARD)
            else onNavigate(HadesDestination.webHubRoute(WebRoutes.DASHBOARD))
        }
        "login" -> {
            if (isWebScreen) webShellController.navigateToUrl(WebRoutes.LOGIN)
            else onNavigate(HadesDestination.webHubRoute(WebRoutes.LOGIN))
        }
        "k0reader" -> onNavigate(HadesDestination.Reader.route)
        "signal" -> onNavigate(HadesDestination.SignalReader.route)
        "gateways" -> onNavigate(HadesDestination.UnderworldGateways.route)
        "notes" -> onNavigate(HadesDestination.FieldNotes.route)
        "localdata" -> onNavigate(HadesDestination.LocalToolData.route)
        "privacy" -> onNavigate(HadesDestination.PrivacySafety.route)
        "hide" -> onHideHex()
        "reset" -> onResetHex()
    }
}

fun tabRouteFromNavRoute(route: String?): String = resolveTabRoute(route ?: HadesDestination.WebHub.tabRoute)
