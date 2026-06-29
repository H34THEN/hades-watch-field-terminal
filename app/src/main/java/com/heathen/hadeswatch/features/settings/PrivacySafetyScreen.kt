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
            "Hades Watch WebShell",
            "Only hadeswatch.com and www.hadeswatch.com load in the trusted WebShell. " +
                "The website remains the source of truth.",
        )
        privacySection(
            "k0R34DER",
            "k0R34DER processes pasted text locally using :k0r34d3r-core. No network upload. " +
                "A legacy local fallback engine remains available in Settings.",
        )
        privacySection(
            "Signal Reader",
            "Signal Reader stores snippets you manually save. It does not scrape Hades Watch or upload text. " +
                "You can send snippet text to k0R34DER locally for RSVP reading.",
        )
        privacySection(
            "Underworld Gateways",
            "Gateway URLs are user-defined and not Hades Watch trusted domains. External browser is the safest default. " +
                "Optional Gateway Viewer is isolated and labeled separately. Gateway URLs do not intentionally receive " +
                "Hades Watch session cookies, though Android WebView cookie isolation has process-level limits.",
        )
        privacySection(
            "Field Notes",
            "Field Notes drafts stay on your device until you copy them manually.",
        )
        privacySection(
            "4R3S Module",
            "4R3S will require explicit permissions only when implemented. MVP does not scan or run background tracking.",
        )
        privacySection(
            "Data Control",
            "Clear website session, WebView cache, local tool data, Signal Reader snippets, gateways, " +
                "k0R34DER preferences, and Gateway Viewer cache separately from Settings.",
        )
        privacySection(
            "Permissions",
            "MVP uses INTERNET and ACCESS_NETWORK_STATE only. No location, Bluetooth, camera, mic, or storage permissions.",
        )
    }
}

@Composable
private fun privacySection(title: String, body: String) {
    HadesTerminalCard(title = title) {
        Text(text = body, style = MaterialTheme.typography.bodyMedium, color = MutedText)
    }
}
