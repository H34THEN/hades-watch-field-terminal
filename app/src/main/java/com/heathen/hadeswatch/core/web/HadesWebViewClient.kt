package com.heathen.hadeswatch.core.web

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class HadesWebViewClient(
    private val openExternalUrl: (String) -> Unit,
    private val onPageStarted: () -> Unit = {},
    private val onPageFinished: () -> Unit = {},
    private val onError: () -> Unit = {},
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        onPageStarted()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        onPageFinished()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: return false
        return handleNavigation(url, view)
    }

    @Deprecated("Deprecated in API")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url == null) return false
        return handleNavigation(url, view)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?,
    ) {
        if (request?.isForMainFrame == true) {
            onError()
        }
    }

    private fun handleNavigation(url: String, view: WebView?): Boolean {
        val uri = android.net.Uri.parse(url)
        val scheme = uri.scheme?.lowercase()

        if (TrustedDomainPolicy.isSafeExternalScheme(scheme)) {
            openExternalUrl(url)
            return true
        }

        if (scheme != "https") {
            openExternalUrl(url)
            return true
        }

        val host = uri.host
        if (TrustedDomainPolicy.isAllowedHost(host)) {
            return false
        }

        openExternalUrl(url)
        return true
    }
}
