package com.heathen.hadeswatch.features.web

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.heathen.hadeswatch.core.hud.HudOverlayContainer
import com.heathen.hadeswatch.core.hud.HudRouteSelectorSheet
import com.heathen.hadeswatch.core.hud.HudWebControls
import com.heathen.hadeswatch.core.navigation.WebRouteOption
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.ui.LoadingOverlay
import com.heathen.hadeswatch.core.ui.OfflineErrorView
import com.heathen.hadeswatch.core.web.HadesWebView
import java.net.URI

@Composable
fun WebHubScreen(
    openExternalInBrowser: Boolean = true,
    initialUrl: String = WebRoutes.DASHBOARD,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedUrl by rememberSaveable { mutableStateOf(initialUrl) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var controlsExpanded by rememberSaveable { mutableStateOf(false) }
    var showRouteSheet by remember { mutableStateOf(false) }

    val hostLabel = remember(selectedUrl) {
        runCatching { URI(selectedUrl).host ?: "hadeswatch.com" }.getOrDefault("hadeswatch.com")
    }

    val openExternal: (String) -> Unit = { target ->
        if (openExternalInBrowser) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
        }
    }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    HudRouteSelectorSheet(
        visible = showRouteSheet,
        currentUrl = selectedUrl,
        onDismiss = { showRouteSheet = false },
        onSelectRoute = { route ->
            selectedUrl = route.url
            hasError = false
            isLoading = true
            reloadKey++
        },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
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

        HudOverlayContainer(
            modifier = Modifier.fillMaxSize(),
            topOverlay = {
                HudWebControls(
                    hostLabel = hostLabel,
                    expanded = controlsExpanded,
                    onToggleExpanded = { controlsExpanded = !controlsExpanded },
                    webView = webView,
                    canGoBack = canGoBack,
                    canGoForward = canGoForward,
                    isLoading = isLoading,
                    progress = progress,
                    onReload = {
                        hasError = false
                        isLoading = true
                        reloadKey++
                    },
                    onOpenRoutes = { showRouteSheet = true },
                    currentUrl = selectedUrl,
                )
            },
        ) { }
    }
}
