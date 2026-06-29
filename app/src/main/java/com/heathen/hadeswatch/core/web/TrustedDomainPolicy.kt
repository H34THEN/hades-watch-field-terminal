package com.heathen.hadeswatch.core.web

object TrustedDomainPolicy {
    private val allowedHosts = setOf("hadeswatch.com", "www.hadeswatch.com")

    fun isAllowedHost(host: String?): Boolean {
        if (host.isNullOrBlank()) return false
        val normalized = host.lowercase().removePrefix("www.")
        return allowedHosts.any { it.removePrefix("www.") == normalized }
    }

    fun isHttps(url: String): Boolean = url.startsWith("https://", ignoreCase = true)

    fun isSafeExternalScheme(scheme: String?): Boolean =
        scheme?.lowercase() in setOf("mailto", "tel")
}
