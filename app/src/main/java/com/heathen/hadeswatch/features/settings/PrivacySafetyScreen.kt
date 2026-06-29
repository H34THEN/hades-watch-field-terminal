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
                "or bypass website auth.",
        )
        privacySection(
            "Native Tools",
            "Native tools are opt-in. k0R34DER processes pasted text locally in MVP — no network upload. " +
                "Field Notes drafts stay on your device until you copy them manually.",
        )
        privacySection(
            "K0R34D3R SDK",
            "Integration with the K0R34D3R SDK is planned through a safe adapter boundary. " +
                "The external SDK is not bundled in MVP.",
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
            "You can clear website session, WebView cache, and local tool data from Settings at any time.",
        )
        privacySection(
            "No Background Tracking",
            "This MVP does not run background location, Bluetooth, Wi-Fi scanning, or upload native tool data without consent.",
        )
    }
}

@Composable
private fun privacySection(title: String, body: String) {
    HadesTerminalCard(title = title) {
        Text(text = body, style = MaterialTheme.typography.bodyMedium, color = MutedText)
    }
}
