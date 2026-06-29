package com.heathen.hadeswatch.core.api

/**
 * Planned Hades Watch mobile API paths. Not called by the app until official endpoints ship.
 */
object MobileApiRoutes {
    const val BASE = "https://hadeswatch.com/api/mobile"

    const val SESSION = "$BASE/session"
    const val NOTIFICATIONS = "$BASE/notifications"
    const val DEAD_DROPS = "$BASE/dead-drops"
    const val DAILY_SIGNALS = "$BASE/daily-signals"
    const val FIELD_LOGS = "$BASE/field-logs"
    const val PROFILE_SUMMARY = "$BASE/profile/summary"
    const val FIELD_NOTES = "$BASE/field-notes"
    const val READING_PROGRESS = "$BASE/reading-progress"

    val all: List<String> = listOf(
        SESSION,
        NOTIFICATIONS,
        DEAD_DROPS,
        DAILY_SIGNALS,
        FIELD_LOGS,
        PROFILE_SUMMARY,
        FIELD_NOTES,
        READING_PROGRESS,
    )
}
