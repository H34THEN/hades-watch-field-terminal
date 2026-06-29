package com.heathen.hadeswatch.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import com.heathen.hadeswatch.features.gateways.GatewayRepository
import com.heathen.hadeswatch.features.gateways.viewer.clearGatewayViewerData
import com.heathen.hadeswatch.features.k0reader.ReaderTransferRepository
import com.heathen.hadeswatch.features.signalreader.SignalSnippetRepository
import kotlinx.coroutines.launch

private data class LocalDataCategory(
    val title: String,
    val description: String,
    val safetyNote: String,
    val actionLabel: String,
    val requiresGatewayWarning: Boolean = false,
)

@Composable
fun LocalToolDataScreen(
    settingsRepository: AppSettingsRepository,
    gatewayRepository: GatewayRepository,
    signalSnippetRepository: SignalSnippetRepository,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var pendingClear by remember { mutableStateOf<LocalDataCategory?>(null) }

    val categories = listOf(
        LocalDataCategory(
            title = "k0R34DER preferences",
            description = "WPM, chunk size, and font size saved for the local reader.",
            safetyNote = "Does not clear pasted text in the reader UI or website session.",
            actionLabel = "Clear k0R34DER preferences",
        ),
        LocalDataCategory(
            title = "k0R34DER transfer text",
            description = "In-memory text handed off from Signal Reader (cleared on consume or app restart).",
            safetyNote = "Temporary only — not persisted to disk.",
            actionLabel = "Clear transfer buffer",
        ),
        LocalDataCategory(
            title = "Signal Reader snippets",
            description = "Locally saved lore snippets and transmissions.",
            safetyNote = "Does not affect Hades Watch website session or Underworld Gateways.",
            actionLabel = "Clear Signal Reader data",
        ),
        LocalDataCategory(
            title = "Underworld Gateways",
            description = "Saved NAS, homelab, and self-hosted gateway URLs.",
            safetyNote = "Does not clear website cookies or Gateway Viewer WebView cache.",
            actionLabel = "Clear gateway list",
        ),
        LocalDataCategory(
            title = "Gateway Viewer cache",
            description = "WebView cache/history from the optional isolated gateway viewer.",
            safetyNote = "Clears a throwaway WebView instance only. Android may share process-level cookie " +
                "storage with other WebViews — see docs/GATEWAY_VIEWER_COOKIE_ISOLATION.md.",
            actionLabel = "Clear Gateway Viewer cache",
            requiresGatewayWarning = true,
        ),
        LocalDataCategory(
            title = "Field Notes draft",
            description = "Local-only field notes draft text.",
            safetyNote = "Does not sync to the website.",
            actionLabel = "Clear Field Notes draft",
        ),
        LocalDataCategory(
            title = "All local tool data",
            description = "Clears k0R34DER prefs, Signal Reader, gateways, and Field Notes draft.",
            safetyNote = "Does not clear Hades Watch website session, WebView cache, or Gateway Viewer cache.",
            actionLabel = "Clear all local tool data",
        ),
    )

    pendingClear?.let { category ->
        AlertDialog(
            onDismissRequest = { pendingClear = null },
            title = { Text(category.actionLabel) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(category.description)
                    Text(category.safetyNote, color = WarningAmber, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        when (category.title) {
                            "k0R34DER preferences" -> settingsRepository.clearK0ReaderPreferences()
                            "k0R34DER transfer text" -> ReaderTransferRepository.clearPending()
                            "Signal Reader snippets" -> signalSnippetRepository.clearAll()
                            "Underworld Gateways" -> gatewayRepository.clearAll()
                            "Gateway Viewer cache" -> clearGatewayViewerData(context)
                            "Field Notes draft" -> settingsRepository.setFieldNotesDraft("")
                            "All local tool data" -> settingsRepository.clearLocalToolData()
                        }
                    }
                    pendingClear = null
                }) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingClear = null }) { Text("Cancel") }
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Local Tool Data",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )
        Text(
            text = "Manage on-device tool data separately from Hades Watch website session.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )
        Text(
            text = "Website session and WebView cache are cleared from Settings → Website Session.",
            style = MaterialTheme.typography.bodyMedium,
            color = OracularViolet,
        )

        categories.forEach { category ->
            HadesTerminalCard(title = category.title) {
                Text(text = category.description, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = category.safetyNote,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(top = 8.dp),
                )
                OutlinedButton(
                    onClick = { pendingClear = category },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                ) {
                    Text(category.actionLabel)
                }
            }
        }
    }
}
