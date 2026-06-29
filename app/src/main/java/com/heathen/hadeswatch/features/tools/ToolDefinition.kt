package com.heathen.hadeswatch.features.tools

enum class ToolStatus {
    Available,
    Planned,
    ComingSoon,
    WebShortcut,
    SettingsShortcut,
}

enum class ToolClassification {
    LOCAL_ONLY,
    WEB_SHORTCUT,
    FUTURE_API,
    PERMISSION_GATED,
    COMING_SOON,
}

enum class ToolCategory {
    READING,
    LAUNCHER,
    OBSERVER,
    NOTES,
    WEB,
    SYSTEM,
    FUTURE,
}

data class ToolDefinition(
    val id: String,
    val name: String,
    val description: String,
    val status: ToolStatus,
    val classification: ToolClassification,
    val category: ToolCategory,
    val iconKey: String,
    val permissionsNeeded: String,
    val safetyNote: String,
    val route: String? = null,
    val webUrl: String? = null,
    val settingsAction: String? = null,
    val settingsToggleKey: String? = null,
)
