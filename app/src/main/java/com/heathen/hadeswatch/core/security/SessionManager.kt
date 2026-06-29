package com.heathen.hadeswatch.core.security

import android.content.Context
import com.heathen.hadeswatch.core.web.clearWebViewCache
import com.heathen.hadeswatch.core.web.clearWebViewSession

class SessionManager(private val context: Context) {
    fun clearWebsiteSession() {
        clearWebViewSession(context)
    }

    fun clearWebViewCache() {
        clearWebViewCache(context)
    }
}
