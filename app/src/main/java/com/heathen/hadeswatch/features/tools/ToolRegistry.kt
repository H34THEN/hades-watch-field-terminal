package com.heathen.hadeswatch.features.tools

import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRoutes

object ToolRegistry {
    val tools: List<ToolDefinition> = listOf(
        ToolDefinition(
            id = "k0reader",
            name = "k0R34DER",
            description = "Local RSVP/Spreeder reader for pasted lore, posts, and field text.",
            permissionsNeeded = "None",
            safetyNote = "Processes pasted text locally. No upload in MVP.",
            status = ToolStatus.Available,
            route = HadesDestination.K0Reader.route,
        ),
        ToolDefinition(
            id = "k0sdk",
            name = "K0R34D3R SDK Adapter",
            description = "Future bridge to the K0R34D3R Flutter SDK repo via adapter boundary.",
            permissionsNeeded = "None (adapter only)",
            safetyNote = "Adapter ready; external SDK not bundled in MVP.",
            status = ToolStatus.Planned,
        ),
        ToolDefinition(
            id = "ares",
            name = "4R3S",
            description = "Passive signal-awareness module (coming soon).",
            permissionsNeeded = "None in MVP",
            safetyNote = "No scanning, no background services in MVP.",
            status = ToolStatus.ComingSoon,
            route = HadesDestination.Ares.route,
        ),
        ToolDefinition(
            id = "fieldnotes",
            name = "Field Notes",
            description = "Local-only draft notes for field ops.",
            permissionsNeeded = "None",
            safetyNote = "Drafts stay on device until you copy them manually.",
            status = ToolStatus.Available,
            route = HadesDestination.FieldNotes.route,
        ),
        ToolDefinition(
            id = "dead_drop_tracker",
            name = "Dead Drop Tracker",
            description = "Web shortcut to Dead Drops on hadeswatch.com.",
            permissionsNeeded = "INTERNET (WebView)",
            safetyNote = "Website remains source of truth.",
            status = ToolStatus.WebShortcut,
            webUrl = WebRoutes.DEAD_DROPS,
        ),
        ToolDefinition(
            id = "signal_reader",
            name = "Signal Reader",
            description = "Placeholder for future signal digest tools.",
            permissionsNeeded = "None",
            safetyNote = "Not implemented in MVP.",
            status = ToolStatus.ComingSoon,
        ),
        ToolDefinition(
            id = "accessibility",
            name = "Accessibility View",
            description = "Jump to Settings for motion, contrast, and text size.",
            permissionsNeeded = "None",
            safetyNote = "Adjusts local UI preferences only.",
            status = ToolStatus.SettingsShortcut,
            settingsAction = HadesDestination.Settings.route,
        ),
    )
}
