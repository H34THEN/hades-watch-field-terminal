package com.heathen.hadeswatch.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesTerminalCard

@Composable
fun PrivacySafetyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Privacy & Safety",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )

        privacySection(
            "Website Authentication",
            "The Hades Watch website handles account login. This app loads hadeswatch.com in a hardened WebView. " +
                "Sessions are preserved through normal Android WebView cookies. The app does not store passwords.",
        )
        privacySection(
            "WebView Companion",
            "The website remains the source of truth. The app does not scrape HTML, duplicate the database, " +
                "or bypass website auth. Only hadeswatch.com and www.hadeswatch.com load in the trusted WebShell.",
        )
        privacySection(
            "k0R34DER",
            "k0R34DER processes pasted text locally using the :k0r34d3r-core Kotlin module. " +
                "No network upload. A legacy local fallback engine remains available in Settings.",
        )
        privacySection(
            "Underworld Gateways",
            "Underworld Gateways stores user-defined NAS/homelab URLs on your device. " +
                "Gateway URLs are not added to the Hades Watch trusted WebView allowlist. " +
                "Launches open externally (or in your browser) and do not inherit Hades Watch session cookies.",
        )
        privacySection(
            "Field Notes",
            "Field Notes drafts stay on your device until you copy them manually. No sync in MVP.",
        )
        privacySection(
            "4R3S Module",
            "4R3S will require explicit permissions only when implemented. MVP does not scan or run background tracking.",
        )
        privacySection(
            "External Links",
            "Links outside hadeswatch.com open in your system browser when enabled in Settings.",
        )
        privacySection(
            "Data Control",
            "You can clear website session, WebView cache, local tool data, k0R34DER preferences, " +
                "and Underworld Gateways from Settings. Clearing local tool data does not clear website cookies " +
                "unless you use Clear website session.",
        )
        privacySection(
            "No Background Tracking",
            "This app does not run background location, Bluetooth, Wi-Fi scanning, or upload native tool data without consent.",
        )
    }
}

@Composable
private fun privacySection(title: String, body: String) {
    HadesTerminalCard(title = title) {
        Text(text = body, style = MaterialTheme.typography.bodyMedium, color = MutedText)
    }
}
