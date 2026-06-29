package com.heathen.hadeswatch.core.hud

import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Bridges WebHubScreen state to the Field Hex command menu.
 * No credentials or URLs are logged.
 */
class WebShellController {
    var isActive by mutableStateOf(false)
    var webView by mutableStateOf<WebView?>(null)
    var canGoBack by mutableStateOf(false)
    var canGoForward by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var currentUrl by mutableStateOf("")
    var hostLabel by mutableStateOf("hadeswatch.com")

    private var reloadCallback: (() -> Unit)? = null
    private var routeSelectorCallback: (() -> Unit)? = null
    private var navigateUrlCallback: ((String) -> Unit)? = null

    fun registerReload(callback: () -> Unit) {
        reloadCallback = callback
    }

    fun registerRouteSelector(callback: () -> Unit) {
        routeSelectorCallback = callback
    }

    fun registerNavigateUrl(callback: (String) -> Unit) {
        navigateUrlCallback = callback
    }

    fun clearWebSession() {
        isActive = false
        webView = null
        canGoBack = false
        canGoForward = false
        isLoading = false
        reloadCallback = null
        navigateUrlCallback = null
    }

    fun unregister() {
        clearWebSession()
        routeSelectorCallback = null
    }

    fun goBack() {
        webView?.goBack()
    }

    fun goForward() {
        webView?.goForward()
    }

    fun reload() {
        reloadCallback?.invoke() ?: webView?.reload()
    }

    fun openRouteSelector() {
        routeSelectorCallback?.invoke()
    }

    fun navigateToUrl(url: String) {
        navigateUrlCallback?.invoke(url)
    }

    fun clear() {
        isActive = false
        webView = null
        canGoBack = false
        canGoForward = false
        isLoading = false
    }
}
