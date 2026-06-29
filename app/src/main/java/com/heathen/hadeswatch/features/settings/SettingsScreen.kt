package com.heathen.hadeswatch.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.security.SessionManager
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen
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
    aresEnabled: Boolean,
    fieldNotesEnabled: Boolean,
    onNavigate: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

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
            SettingsAction("Open Hades Watch login") {
                onNavigate(HadesDestination.webRoute(WebRoutes.LOGIN))
            }
            SettingsAction("Clear website session") {
                sessionManager.clearWebsiteSession()
            }
            SettingsAction("Clear WebView cache") {
                sessionManager.clearWebViewCache()
            }
            SettingsAction("Clear local tool data") {
                scope.launch { settingsRepository.clearLocalToolData() }
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
            SettingsToggle("Open external links in browser", openExternalInBrowser) {
                scope.launch { settingsRepository.setOpenExternalInBrowser(it) }
            }
        }

        HadesTerminalCard(title = "Tools") {
            SettingsToggle("Enable k0R34DER", k0ReaderEnabled) {
                scope.launch { settingsRepository.setK0ReaderEnabled(it) }
            }
            SettingsToggle("Show 4R3S module", aresEnabled) {
                scope.launch { settingsRepository.setAresEnabled(it) }
            }
            SettingsToggle("Enable Field Notes", fieldNotesEnabled) {
                scope.launch { settingsRepository.setFieldNotesEnabled(it) }
            }
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
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
