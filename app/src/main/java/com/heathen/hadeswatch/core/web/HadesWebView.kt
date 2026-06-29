package com.heathen.hadeswatch.core.web

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

const val HADES_USER_AGENT_SUFFIX = " HadesWatchAndroid/0.1"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HadesWebView(
    url: String,
    modifier: Modifier = Modifier,
    openExternalUrl: (String) -> Unit,
    onCanGoBackChanged: (Boolean) -> Unit = {},
    onLoadingChanged: (Boolean) -> Unit = {},
    onProgressChanged: (Int) -> Unit = {},
    onError: () -> Unit = {},
    webViewRef: (WebView?) -> Unit = {},
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var loadProgress by remember { mutableIntStateOf(0) }

    DisposableEffect(webView) {
        onDispose {
            webViewRef(null)
        }
    }

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
                    databaseEnabled = true
                    allowFileAccess = false
                    allowContentAccess = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        allowFileAccessFromFileURLs = false
                        allowUniversalAccessFromFileURLs = false
                    }
                    mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = false
                    displayZoomControls = false
                    setSupportMultipleWindows(false)

                    val defaultUa = userAgentString ?: ""
                    if (!defaultUa.contains("HadesWatchAndroid")) {
                        userAgentString = defaultUa + HADES_USER_AGENT_SUFFIX
                    }
                }

                if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_ENABLE)) {
                    WebSettingsCompat.setSafeBrowsingEnabled(settings, true)
                }

                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, false)

                webViewClient = HadesWebViewClient(
                    openExternalUrl = openExternalUrl,
                    onPageStarted = {
                        onLoadingChanged(true)
                    },
                    onPageFinished = {
                        onLoadingChanged(false)
                        onCanGoBackChanged(canGoBack())
                    },
                    onError = onError,
                )

                webChromeClient = HadesWebChromeClient { progress ->
                    loadProgress = progress
                    onProgressChanged(progress)
                    if (progress >= 100) {
                        onLoadingChanged(false)
                    }
                }

                loadUrl(url)
                webView = this
                webViewRef(this)
            }
        },
        update = { view ->
            webView = view
            webViewRef(view)
        },
    )
}

fun clearWebViewSession(context: android.content.Context) {
    CookieManager.getInstance().removeAllCookies(null)
    CookieManager.getInstance().flush()
}

fun clearWebViewCache(context: android.content.Context) {
    WebView(context).apply {
        clearCache(true)
        clearHistory()
        destroy()
    }
}
