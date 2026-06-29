package com.heathen.hadeswatch.features.webshell

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.heathen.hadeswatch.core.ui.LoadingOverlay
import com.heathen.hadeswatch.core.ui.OfflineErrorView
import com.heathen.hadeswatch.core.web.HadesWebView

@Composable
fun WebShellScreen(
    url: String,
    openExternalInBrowser: Boolean = true,
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var reloadKey by remember { mutableIntStateOf(0) }

    val openExternal: (String) -> Unit = { target ->
        if (openExternalInBrowser) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
        }
    }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasError) {
            OfflineErrorView(onRetry = {
                hasError = false
                isLoading = true
                reloadKey++
            })
        } else {
            androidx.compose.runtime.key(reloadKey) {
                HadesWebView(
                    url = url,
                    modifier = Modifier.fillMaxSize(),
                    openExternalUrl = openExternal,
                    onCanGoBackChanged = { canGoBack = it },
                    onLoadingChanged = { isLoading = it },
                    onProgressChanged = { progress = it },
                    onError = { hasError = true },
                    webViewRef = { webView = it },
                )
            }
            LoadingOverlay(visible = isLoading && progress < 100, progress = progress)
        }
    }
}
