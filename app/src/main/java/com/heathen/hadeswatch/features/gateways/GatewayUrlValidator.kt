package com.heathen.hadeswatch.features.gateways

object GatewayUrlValidator {
    private val urlPattern = Regex("^https?://[^\\s/$.?#].[^\\s]*", RegexOption.IGNORE_CASE)

    fun isValid(url: String): Boolean {
        val trimmed = url.trim()
        if (!trimmed.startsWith("http://", ignoreCase = true) &&
            !trimmed.startsWith("https://", ignoreCase = true)
        ) {
            return false
        }
        return urlPattern.matches(trimmed) || trimmed.matches(Regex("^https?://[^\\s]+$", RegexOption.IGNORE_CASE))
    }

    fun isHttps(url: String): Boolean = url.trim().startsWith("https://", ignoreCase = true)

    fun isLocalLan(url: String): Boolean {
        val host = hostOf(url)?.lowercase() ?: return false
        return host == "localhost" ||
            host.endsWith(".local") ||
            host.startsWith("192.168.") ||
            host.startsWith("10.") ||
            host.matches(Regex("^172\\.(1[6-9]|2\\d|3[01])\\..*"))
    }

    fun hostOf(url: String): String? =
        runCatching { android.net.Uri.parse(url.trim()).host }.getOrNull()

    fun normalize(url: String): String = url.trim()

    fun warningFor(url: String): String? = when {
        !isValid(url) -> "URL must start with http:// or https://"
        !isHttps(url) && isLocalLan(url) -> "HTTP on local/LAN — not encrypted"
        !isHttps(url) -> "HTTP is not encrypted — use HTTPS when possible"
        else -> null
    }
}
