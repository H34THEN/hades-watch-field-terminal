package com.heathen.hadeswatch.features.tools

enum class ToolStatus {
    Available,
    Planned,
    ComingSoon,
    WebShortcut,
    SettingsShortcut,
}

data class ToolDefinition(
    val id: String,
    val name: String,
    val description: String,
    val permissionsNeeded: String,
    val safetyNote: String,
    val status: ToolStatus,
    val route: String? = null,
    val webUrl: String? = null,
    val settingsAction: String? = null,
)
