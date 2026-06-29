package com.heathen.hadeswatch.features.gateways

object GatewayUrlValidator {
    private val urlPattern = Regex("^https?://.+", RegexOption.IGNORE_CASE)

    fun isValid(url: String): Boolean = urlPattern.matches(url.trim())

    fun isHttps(url: String): Boolean = url.trim().startsWith("https://", ignoreCase = true)

    fun normalize(url: String): String = url.trim()
}
