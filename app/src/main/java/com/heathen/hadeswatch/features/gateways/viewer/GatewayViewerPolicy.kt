package com.heathen.hadeswatch.features.gateways.viewer

import android.net.Uri

/**
 * Policy for the isolated Underworld Gateway Viewer — separate from Hades Watch WebShell.
 */
object GatewayViewerPolicy {

    fun isSameHost(currentUrl: String, targetUrl: String): Boolean {
        val currentHost = runCatching { Uri.parse(currentUrl).host?.lowercase() }.getOrNull()
        val targetHost = runCatching { Uri.parse(targetUrl).host?.lowercase() }.getOrNull()
        return !currentHost.isNullOrBlank() && currentHost == targetHost
    }

    fun isHttps(url: String): Boolean = url.trim().startsWith("https://", ignoreCase = true)

    fun isSafeExternalScheme(scheme: String?): Boolean =
        scheme?.lowercase() in setOf("mailto", "tel")

    fun hostLabel(url: String): String =
        runCatching { Uri.parse(url).host ?: url }.getOrDefault(url)
}
