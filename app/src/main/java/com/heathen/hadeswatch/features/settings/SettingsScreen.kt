package com.heathen.hadeswatch.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.security.SessionManager
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    settingsRepository: AppSettingsRepository,
    sessionManager: SessionManager,
    reducedMotion: Boolean,
    highContrast: Boolean,
    largeText: Boolean,
    openExternalInBrowser: Boolean,
    k0ReaderEnabled: Boolean,
    k0ReaderUseSdk: Boolean,
    gatewaysEnabled: Boolean,
    signalReaderEnabled: Boolean,
    aresEnabled: Boolean,
    fieldNotesEnabled: Boolean,
    onNavigate: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val fieldHexEnabled by settingsRepository.fieldHexEnabled.collectAsState(initial = true)
    val fieldHexSize by settingsRepository.fieldHexSize.collectAsState(initial = 1)
    val fieldHexOpacity by settingsRepository.fieldHexOpacity.collectAsState(initial = 1)
    val fieldHexShowSafetyChip by settingsRepository.fieldHexShowSafetyChip.collectAsState(initial = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )

        HadesTerminalCard(title = "Website Session") {
            Text(
                text = "Hades Watch WebShell session — separate from local tool data.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            SettingsAction("Open Hades Watch login") {
                onNavigate(HadesDestination.webRoute(WebRoutes.LOGIN))
            }
            SettingsAction("Clear website session (cookies)") {
                sessionManager.clearWebsiteSession()
            }
            SettingsAction("Clear Hades Watch WebView cache") {
                sessionManager.clearWebViewCache()
            }
        }

        HadesTerminalCard(title = "Local Tool Data") {
            Text(
                text = "Clear k0R34DER, Signal Reader, gateways, and Field Notes without touching website session.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            SettingsAction("Manage local tool data") {
                onNavigate(HadesDestination.LocalToolData.route)
            }
        }

        HadesTerminalCard(title = "Future API Status") {
            Text(
                text = "Planned mobile endpoints — not called until official APIs ship with user consent.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            SettingsAction("View future API status") {
                onNavigate(HadesDestination.FutureApiStatus.route)
            }
        }

        HadesTerminalCard(title = "Field Hex") {
            Text(
                text = "Draggable command orb for navigation and web controls. Long-press the hex to reset its position.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            SettingsToggle("Enable Field Hex", fieldHexEnabled) {
                scope.launch { settingsRepository.setFieldHexEnabled(it) }
            }
            SettingsToggle("Show brief safety chip on web load", fieldHexShowSafetyChip) {
                scope.launch { settingsRepository.setFieldHexShowSafetyChip(it) }
            }
            HexSizeSelector("Hex size", fieldHexSize) {
                scope.launch { settingsRepository.setFieldHexSize(it) }
            }
            HexSizeSelector("Hex opacity", fieldHexOpacity) {
                scope.launch { settingsRepository.setFieldHexOpacity(it) }
            }
            SettingsAction("Reset Field Hex position") {
                scope.launch { settingsRepository.resetFieldHexPosition() }
            }
        }

        HadesTerminalCard(title = "Display & Accessibility") {
            SettingsToggle("Reduced motion", reducedMotion) {
                scope.launch { settingsRepository.setReducedMotion(it) }
            }
            SettingsToggle("High contrast", highContrast) {
                scope.launch { settingsRepository.setHighContrast(it) }
            }
            SettingsToggle("Large text", largeText) {
                scope.launch { settingsRepository.setLargeText(it) }
            }
            Text(
                text = "Large text uses spacious Tools Hub cards; off uses compact layout.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            SettingsToggle("Open external links in browser", openExternalInBrowser) {
                scope.launch { settingsRepository.setOpenExternalInBrowser(it) }
            }
        }

        HadesTerminalCard(title = "Tools") {
            SettingsToggle("Enable k0R34DER", k0ReaderEnabled) {
                scope.launch { settingsRepository.setK0ReaderEnabled(it) }
            }
            SettingsToggle("Use K0R34D3R Kotlin core", k0ReaderUseSdk) {
                scope.launch { settingsRepository.setK0ReaderUseSdkAdapter(it) }
            }
            SettingsToggle("Enable Signal Reader", signalReaderEnabled) {
                scope.launch { settingsRepository.setSignalReaderEnabled(it) }
            }
            SettingsToggle("Enable Underworld Gateways", gatewaysEnabled) {
                scope.launch { settingsRepository.setGatewaysEnabled(it) }
            }
            SettingsToggle("Show 4R3S module", aresEnabled) {
                scope.launch { settingsRepository.setAresEnabled(it) }
            }
            SettingsToggle("Enable Field Notes", fieldNotesEnabled) {
                scope.launch { settingsRepository.setFieldNotesEnabled(it) }
            }
        }

        HadesTerminalCard(title = "Native Tool Notes") {
            Text(
                text = "k0R34DER and Signal Reader process text locally. No upload. Signal Reader supports JSON import/export on device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            Text(
                text = "Underworld Gateways external browser is the safest default. Gateway Viewer cache clear does not call Hades Watch session clear — see cookie isolation doc.",
                style = MaterialTheme.typography.bodyMedium,
                color = WarningAmber,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        HadesTerminalCard(title = "Privacy") {
            Text(
                text = "Privacy & Safety",
                style = MaterialTheme.typography.titleMedium,
                color = TerminalGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(HadesDestination.PrivacySafety.route) }
                    .padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun HexSizeSelector(label: String, selectedIndex: Int, onSelect: (Int) -> Unit) {
    val options = listOf("Small", "Medium", "Large")
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
            options.forEachIndexed { index, option ->
                Text(
                    text = option,
                    color = if (index == selectedIndex) TerminalGreen else MutedText,
                    modifier = Modifier
                        .clickable { onSelect(index) }
                        .padding(8.dp),
                )
            }
        }
    }
}

@Composable
private fun SettingsAction(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
    )
    HorizontalDivider(color = MutedText.copy(alpha = 0.3f))
}

@Composable
private fun SettingsToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { value ->
            scope.launch { onCheckedChange(value) }
        })
    }
}
