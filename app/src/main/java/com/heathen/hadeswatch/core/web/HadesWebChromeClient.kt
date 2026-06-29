package com.heathen.hadeswatch.core.web

import android.webkit.WebChromeClient
import android.webkit.WebView

class HadesWebChromeClient(
    private val onProgressChanged: (Int) -> Unit = {},
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        onProgressChanged(newProgress)
    }
}
