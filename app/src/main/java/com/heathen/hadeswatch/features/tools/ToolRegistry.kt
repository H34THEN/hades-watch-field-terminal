package com.heathen.hadeswatch.features.tools

import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.settings.SettingsKeys

object ToolRegistry {
    val tools: List<ToolDefinition> = listOf(
        ToolDefinition(
            id = "k0reader",
            name = "k0R34DER",
            description = "Local RSVP/Spreeder reader for pasted lore, posts, and field text.",
            status = ToolStatus.Available,
            classification = ToolClassification.LOCAL_ONLY,
            category = ToolCategory.READING,
            iconKey = "k0reader",
            permissionsNeeded = "None",
            safetyNote = "Processes pasted text locally via K0R34D3R Kotlin core. No upload.",
            route = HadesDestination.Reader.route,
            settingsToggleKey = SettingsKeys.TOOL_K0READER_ENABLED,
        ),
        ToolDefinition(
            id = "signal_reader",
            name = "Signal Reader",
            description = "Local library for manually saved lore snippets and transmissions.",
            status = ToolStatus.Available,
            classification = ToolClassification.LOCAL_ONLY,
            category = ToolCategory.READING,
            iconKey = "signal_reader",
            permissionsNeeded = "None",
            safetyNote = "Local-only snippets. JSON import/export stays on device. Does not scrape websites.",
            route = HadesDestination.SignalReader.route,
            settingsToggleKey = SettingsKeys.TOOL_SIGNAL_READER_ENABLED,
        ),
        ToolDefinition(
            id = "gateways",
            name = "Underworld Gateways",
            description = "Save and launch trusted local NAS, homelab, and self-hosted URLs.",
            status = ToolStatus.Available,
            classification = ToolClassification.LOCAL_ONLY,
            category = ToolCategory.LAUNCHER,
            iconKey = "gateways",
            permissionsNeeded = "None in MVP",
            safetyNote = "User-defined URLs — external browser default; optional isolated viewer.",
            route = HadesDestination.UnderworldGateways.route,
            settingsToggleKey = SettingsKeys.TOOL_GATEWAYS_ENABLED,
            hubSection = ToolHubSection.SELF_HOSTED_GATEWAY,
        ),
        ToolDefinition(
            id = "fieldnotes",
            name = "Field Notes",
            description = "Local-only draft notes for field ops.",
            status = ToolStatus.Available,
            classification = ToolClassification.LOCAL_ONLY,
            category = ToolCategory.NOTES,
            iconKey = "fieldnotes",
            permissionsNeeded = "None",
            safetyNote = "Drafts stay on device until you copy them manually.",
            route = HadesDestination.FieldNotes.route,
            settingsToggleKey = SettingsKeys.TOOL_FIELD_NOTES_ENABLED,
        ),
        ToolDefinition(
            id = "dead_drop_tracker",
            name = "Dead Drop Tracker",
            description = "Web shortcut to Dead Drops on hadeswatch.com.",
            status = ToolStatus.WebShortcut,
            classification = ToolClassification.WEB_SHORTCUT,
            category = ToolCategory.WEB,
            iconKey = "dead_drops",
            permissionsNeeded = "INTERNET (WebView)",
            safetyNote = "Website remains source of truth. Trusted domain WebShell only.",
            webUrl = WebRoutes.DEAD_DROPS,
            hubSection = ToolHubSection.WEB_SHORTCUT,
        ),
        ToolDefinition(
            id = "ares",
            name = "4R3S",
            description = "Passive signal-awareness module (coming soon).",
            status = ToolStatus.ComingSoon,
            classification = ToolClassification.PERMISSION_GATED,
            category = ToolCategory.OBSERVER,
            iconKey = "ares",
            permissionsNeeded = "Future: TBD (not requested in MVP)",
            safetyNote = "No scanning, no background services in MVP.",
            route = HadesDestination.Ares.route,
            settingsToggleKey = SettingsKeys.TOOL_ARES_ENABLED,
            hubSection = ToolHubSection.PERMISSION_GATED,
        ),
        ToolDefinition(
            id = "accessibility",
            name = "Accessibility View",
            description = "Jump to Settings for motion, contrast, and text size.",
            status = ToolStatus.SettingsShortcut,
            classification = ToolClassification.LOCAL_ONLY,
            category = ToolCategory.SYSTEM,
            iconKey = "accessibility",
            permissionsNeeded = "None",
            safetyNote = "Adjusts local UI preferences only.",
            settingsAction = HadesDestination.Settings.route,
            hubSection = ToolHubSection.LOCAL_TOOLS,
        ),
    )

    fun visibleTools(
        k0ReaderEnabled: Boolean,
        gatewaysEnabled: Boolean,
        signalReaderEnabled: Boolean,
        aresEnabled: Boolean,
        fieldNotesEnabled: Boolean,
    ): List<ToolDefinition> = tools.filter { tool ->
        when (tool.id) {
            "k0reader" -> k0ReaderEnabled
            "gateways" -> gatewaysEnabled
            "signal_reader" -> signalReaderEnabled
            "ares" -> aresEnabled
            "fieldnotes" -> fieldNotesEnabled
            else -> true
        }
    }

    fun groupedVisibleTools(
        k0ReaderEnabled: Boolean,
        gatewaysEnabled: Boolean,
        signalReaderEnabled: Boolean,
        aresEnabled: Boolean,
        fieldNotesEnabled: Boolean,
    ): List<Pair<String, List<ToolDefinition>>> {
        val visible = visibleTools(k0ReaderEnabled, gatewaysEnabled, signalReaderEnabled, aresEnabled, fieldNotesEnabled)
        return listOf(
            "Available" to visible.filter {
                it.status == ToolStatus.Available &&
                    it.classification == ToolClassification.LOCAL_ONLY &&
                    it.id != "accessibility"
            },
            "Website Shortcuts" to visible.filter {
                it.classification == ToolClassification.WEB_SHORTCUT
            },
            "Coming Soon" to visible.filter {
                it.status == ToolStatus.ComingSoon ||
                    it.classification == ToolClassification.PERMISSION_GATED
            },
            "Support" to visible.filter {
                it.id == "accessibility" || it.status == ToolStatus.SettingsShortcut
            },
        ).filter { it.second.isNotEmpty() }
    }
}
