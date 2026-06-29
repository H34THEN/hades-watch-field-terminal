package com.heathen.hadeswatch.features.web

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.WebRouteOption
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.navigation.primaryWebRoutes
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesIcon
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.LoadingOverlay
import com.heathen.hadeswatch.core.ui.OfflineErrorView
import com.heathen.hadeswatch.core.web.HadesWebView
import java.net.URI

@Composable
fun WebHubScreen(
    openExternalInBrowser: Boolean = true,
    initialUrl: String = WebRoutes.DASHBOARD,
) {
    val context = LocalContext.current
    var selectedUrl by remember { mutableStateOf(initialUrl) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var reloadKey by remember { mutableIntStateOf(0) }

    val hostLabel = remember(selectedUrl) {
        runCatching { URI(selectedUrl).host ?: selectedUrl }.getOrDefault(selectedUrl)
    }

    val openExternal: (String) -> Unit = { target ->
        if (openExternalInBrowser) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
        }
    }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            HadesSectionHeader(
                title = "Hades Watch Web",
                subtitle = "Trusted domain WebShell — hadeswatch.com only",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = hostLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TerminalGreen,
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = { webView?.goBack() },
                    enabled = canGoBack,
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                IconButton(
                    onClick = { webView?.goForward() },
                    enabled = canGoForward,
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward")
                }
                IconButton(onClick = {
                    hasError = false
                    isLoading = true
                    reloadKey++
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reload")
                }
            }
            if (isLoading && progress in 1..99) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                primaryWebRoutes.forEach { route ->
                    WebRouteChip(
                        route = route,
                        selected = selectedUrl == route.url,
                        onSelect = {
                            selectedUrl = route.url
                            hasError = false
                            isLoading = true
                        },
                    )
                }
            }
        }

        if (hasError) {
            OfflineErrorView(onRetry = {
                hasError = false
                isLoading = true
                reloadKey++
            })
        } else {
            key(selectedUrl, reloadKey) {
                BoxWebContent(
                    url = selectedUrl,
                    openExternal = openExternal,
                    onWebView = { webView = it },
                    onCanGoBack = { canGoBack = it },
                    onCanGoForward = { canGoForward = it },
                    onLoading = { isLoading = it },
                    onProgress = { progress = it },
                    onError = { hasError = true },
                )
            }
            LoadingOverlay(visible = isLoading && progress < 100, progress = progress)
        }
    }
}

@Composable
private fun WebRouteChip(
    route: WebRouteOption,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onSelect,
        label = { Text(route.label) },
        leadingIcon = {
            HadesIcon(key = route.iconKey, contentDescription = null)
        },
    )
}

@Composable
private fun BoxWebContent(
    url: String,
    openExternal: (String) -> Unit,
    onWebView: (WebView?) -> Unit,
    onCanGoBack: (Boolean) -> Unit,
    onCanGoForward: (Boolean) -> Unit,
    onLoading: (Boolean) -> Unit,
    onProgress: (Int) -> Unit,
    onError: () -> Unit,
) {
    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
        HadesWebView(
            url = url,
            modifier = Modifier.fillMaxSize(),
            openExternalUrl = openExternal,
            onCanGoBackChanged = onCanGoBack,
            onCanGoForwardChanged = onCanGoForward,
            onLoadingChanged = onLoading,
            onProgressChanged = onProgress,
            onError = onError,
            webViewRef = onWebView,
        )
    }
}
