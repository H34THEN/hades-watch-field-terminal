package com.heathen.hadeswatch.features.gateways.viewer

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.heathen.hadeswatch.core.web.HadesWebChromeClient

private const val GATEWAY_USER_AGENT_SUFFIX = " HadesWatchGatewayViewer/0.1"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GenericGatewayWebView(
    url: String,
    modifier: Modifier = Modifier,
    openExternalUrl: (String) -> Unit,
    onLoadingChanged: (Boolean) -> Unit = {},
    onProgressChanged: (Int) -> Unit = {},
    onError: () -> Unit = {},
) {
    val gatewayHost = remember(url) { GatewayViewerPolicy.hostLabel(url) }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = false
                    allowContentAccess = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        allowFileAccessFromFileURLs = false
                        allowUniversalAccessFromFileURLs = false
                    }
                    mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = false
                    displayZoomControls = false
                    setSupportMultipleWindows(false)
                    val defaultUa = userAgentString ?: ""
                    if (!defaultUa.contains("HadesWatchGatewayViewer")) {
                        userAgentString = defaultUa + GATEWAY_USER_AGENT_SUFFIX
                    }
                }
                if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_ENABLE)) {
                    WebSettingsCompat.setSafeBrowsingEnabled(settings, true)
                }
                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, false)
                webViewClient = GenericGatewayWebViewClient(
                    gatewayHost = gatewayHost,
                    openExternalUrl = openExternalUrl,
                    onPageStarted = { onLoadingChanged(true) },
                    onPageFinished = { onLoadingChanged(false) },
                    onError = onError,
                )
                webChromeClient = HadesWebChromeClient { progress ->
                    onProgressChanged(progress)
                    if (progress >= 100) onLoadingChanged(false)
                }
                loadUrl(url)
            }
        },
        update = { view ->
            // Reload only if URL changed externally
        },
    )
}

fun clearGatewayViewerData(context: android.content.Context) {
    // Clears cache/history on a throwaway WebView only — does NOT call removeAllCookies().
    // Hades Watch WebShell session clear is a separate Settings action.
    WebView(context).apply {
        clearCache(true)
        clearHistory()
        destroy()
    }
}
