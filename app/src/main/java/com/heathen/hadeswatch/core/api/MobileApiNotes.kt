package com.heathen.hadeswatch.core.api

/**
 * In-app copy for future API integration — documentation only, no network layer.
 */
object MobileApiNotes {
    const val HEADLINE = "Future API Status"
    const val SUMMARY =
        "Recommended Hades Watch mobile endpoints are documented for planning. " +
            "They are not assumed to exist and are not called by this app."
    const val WEBSITE_TRUTH =
        "https://hadeswatch.com remains the source of truth for gameplay and account data."
    const val LOCAL_TOOLS =
        "Native tools (k0R34DER, Signal Reader, Underworld Gateways, Field Notes) stay local-only until " +
            "official APIs ship and you opt in."
    const val CONSENT =
        "Upload endpoints (Field Notes, reading progress) will require explicit user consent before any sync."
    const val NO_POLLING = "No background polling, scraping, or credential storage is implemented."
}
