package com.heathen.hadeswatch.core.api

enum class MobileApiCapability {
    SESSION,
    NOTIFICATIONS,
    DEAD_DROPS,
    DAILY_SIGNALS,
    FIELD_LOGS,
    PROFILE_SUMMARY,
    FIELD_NOTES_SYNC,
    READING_PROGRESS_SYNC,
}

data class MobileApiCapabilityInfo(
    val capability: MobileApiCapability,
    val route: String,
    val method: String,
    val summary: String,
    val requiresUserConsent: Boolean = false,
)

object MobileApiCapabilityRegistry {
    val planned: List<MobileApiCapabilityInfo> = listOf(
        MobileApiCapabilityInfo(
            MobileApiCapability.SESSION,
            MobileApiRoutes.SESSION,
            "GET",
            "Validate mobile session / profile stub",
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.NOTIFICATIONS,
            MobileApiRoutes.NOTIFICATIONS,
            "GET",
            "Native notification summary (future)",
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.DEAD_DROPS,
            MobileApiRoutes.DEAD_DROPS,
            "GET",
            "Dead drop highlights for widgets",
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.DAILY_SIGNALS,
            MobileApiRoutes.DAILY_SIGNALS,
            "GET",
            "Daily signals digest",
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.FIELD_LOGS,
            MobileApiRoutes.FIELD_LOGS,
            "GET",
            "Field log entries",
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.PROFILE_SUMMARY,
            MobileApiRoutes.PROFILE_SUMMARY,
            "GET",
            "Lightweight profile card",
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.FIELD_NOTES_SYNC,
            MobileApiRoutes.FIELD_NOTES,
            "POST",
            "Opt-in Field Notes sync",
            requiresUserConsent = true,
        ),
        MobileApiCapabilityInfo(
            MobileApiCapability.READING_PROGRESS_SYNC,
            MobileApiRoutes.READING_PROGRESS,
            "POST",
            "Opt-in k0R34DER reading progress",
            requiresUserConsent = true,
        ),
    )
}
