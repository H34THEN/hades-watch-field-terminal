package com.heathen.hadeswatch.features.web

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.hud.HudStatusChip
import com.heathen.hadeswatch.core.hud.WebShellController
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.ui.LoadingOverlay
import com.heathen.hadeswatch.core.ui.OfflineErrorView
import com.heathen.hadeswatch.core.web.HadesWebView
import java.net.URI
import kotlinx.coroutines.delay

@Composable
fun WebHubScreen(
    webShellController: WebShellController,
    openExternalInBrowser: Boolean = true,
    initialUrl: String = WebRoutes.DASHBOARD,
    showSafetyChip: Boolean = true,
) {
    val context = LocalContext.current
    var selectedUrl by rememberSaveable { mutableStateOf(initialUrl) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var showSafetyBanner by remember { mutableStateOf(showSafetyChip) }

    val hostLabel = remember(selectedUrl) {
        runCatching { URI(selectedUrl).host ?: "hadeswatch.com" }.getOrDefault("hadeswatch.com")
    }

    val openExternal: (String) -> Unit = { target ->
        if (openExternalInBrowser) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
        }
    }

    DisposableEffect(Unit) {
        webShellController.isActive = true
        webShellController.registerReload {
            hasError = false
            isLoading = true
            reloadKey++
        }
        webShellController.registerNavigateUrl { url ->
            selectedUrl = url
            hasError = false
            isLoading = true
            reloadKey++
        }
        onDispose {
            webShellController.clearWebSession()
        }
    }

    LaunchedEffect(webView, canGoBack, canGoForward, isLoading, selectedUrl, hostLabel) {
        webShellController.webView = webView
        webShellController.canGoBack = canGoBack
        webShellController.canGoForward = canGoForward
        webShellController.isLoading = isLoading
        webShellController.currentUrl = selectedUrl
        webShellController.hostLabel = hostLabel
    }

    LaunchedEffect(showSafetyChip, selectedUrl) {
        if (showSafetyChip) {
            showSafetyBanner = true
            delay(3500)
            showSafetyBanner = false
        }
    }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Bottom,
                ),
            ),
    ) {
        if (hasError) {
            OfflineErrorView(onRetry = {
                hasError = false
                isLoading = true
                reloadKey++
            })
        } else {
            key(selectedUrl, reloadKey) {
                HadesWebView(
                    url = selectedUrl,
                    modifier = Modifier.fillMaxSize(),
                    openExternalUrl = openExternal,
                    onCanGoBackChanged = { canGoBack = it },
                    onCanGoForwardChanged = { canGoForward = it },
                    onLoadingChanged = { isLoading = it },
                    onProgressChanged = { progress = it },
                    onError = { hasError = true },
                    webViewRef = { webView = it },
                )
            }
            LoadingOverlay(visible = isLoading && progress < 100, progress = progress)
        }

        AnimatedVisibility(
            visible = showSafetyBanner,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
        ) {
            HudStatusChip(
                label = "Hades Watch · $hostLabel",
                onClick = { showSafetyBanner = false },
                showShield = true,
            )
        }
    }
}
