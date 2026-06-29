package com.heathen.hadeswatch.features.gateways

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesEmptyState
import com.heathen.hadeswatch.core.ui.HadesIcon
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import com.heathen.hadeswatch.core.ui.HadesWarningBox
import com.heathen.hadeswatch.core.ui.ToolIconKey
import kotlinx.coroutines.launch

@Composable
fun UnderworldGatewaysScreen(
    gatewayRepository: GatewayRepository,
    onAddGateway: () -> Unit,
    onEditGateway: (String) -> Unit,
    onOpenInAppViewer: (GatewayDefinition) -> Unit,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val gateways by gatewayRepository.gateways.collectAsState(initial = emptyList())

    var pendingLaunch by remember { mutableStateOf<GatewayDefinition?>(null) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importJson by remember { mutableStateOf("") }
    var importPreview by remember { mutableStateOf<GatewayStorage.ImportPreview?>(null) }

    pendingLaunch?.let { gateway ->
        val isHttp = !GatewayUrlValidator.isHttps(gateway.url)
        AlertDialog(
            onDismissRequest = { pendingLaunch = null },
            title = { Text(if (isHttp) "Launch HTTP gateway" else "Launch gateway") },
            text = {
                Text(
                    buildString {
                        append("Destination: ${gateway.url}\n\n")
                        if (isHttp) append("HTTP is not encrypted. ")
                        append("This does not use Hades Watch session cookies.\n")
                        append("Mode: ${gateway.launchMode.name.replace('_', ' ')}")
                    },
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch { gatewayRepository.markOpened(gateway.id) }
                    GatewayLauncher.launch(context, gateway, onOpenInAppViewer)
                    pendingLaunch = null
                }) {
                    Text("Launch")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingLaunch = null }) { Text("Cancel") }
            },
        )
    }

    importPreview?.let { preview ->
        AlertDialog(
            onDismissRequest = { importPreview = null },
            title = { Text("Import gateways") },
            text = {
                Text(
                    "Found ${preview.total} entries (${preview.validCount} valid, " +
                        "${preview.invalidCount} invalid). Merge without overwriting existing IDs?",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        gatewayRepository.mergeImport(preview.gateways, overwrite = false)
                        importPreview = null
                        showImportDialog = false
                    }
                }) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { importPreview = null }) { Text("Cancel") }
            },
        )
    }

    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Paste gateway JSON") },
            text = {
                Column {
                    Text("Paste exported JSON. URLs are validated on import.", color = MutedText)
                    androidx.compose.material3.OutlinedTextField(
                        value = importJson,
                        onValueChange = { importJson = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        minLines = 4,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    importPreview = GatewayStorage.validateImportJson(importJson)
                }) {
                    Text("Preview")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) { Text("Cancel") }
            },
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddGateway) {
                Icon(Icons.Default.Add, contentDescription = "Add gateway")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    HadesIcon(key = ToolIconKey.GATEWAY, contentDescription = "Gateways")
                    Text(
                        text = "Underworld Gateways",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OracularViolet,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
                Text(
                    text = "NAS & homelab panel — separate from Hades Watch WebShell.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = {
                        val json = gatewayRepository.exportJson(gateways)
                        clipboard.setText(AnnotatedString(json))
                    }) {
                        Icon(Icons.Default.FileDownload, contentDescription = null)
                        Text("Copy export", modifier = Modifier.padding(start = 4.dp))
                    }
                    OutlinedButton(onClick = {
                        val json = gatewayRepository.exportJson(gateways)
                        context.startActivity(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "application/json"
                                putExtra(Intent.EXTRA_TEXT, json)
                                putExtra(Intent.EXTRA_SUBJECT, "Underworld Gateways backup")
                            },
                        )
                    }) {
                        Text("Share JSON")
                    }
                    OutlinedButton(onClick = { showImportDialog = true }) {
                        Icon(Icons.Default.FileUpload, contentDescription = null)
                        Text("Import", modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            if (gateways.isEmpty()) {
                item {
                    HadesEmptyState(
                        title = "No gateways yet",
                        message = "Add trusted local services such as Jellyfin, Home Assistant, NAS dashboards, MeTube, or slskd.",
                    )
                }
            }

            items(gateways, key = { it.id }) { gateway ->
                HadesTerminalCard(title = gateway.displayName) {
                    Row {
                        GatewayIconDisplay(gateway = gateway)
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(text = gateway.url, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = GatewayUrlValidator.hostOf(gateway.url) ?: "",
                                style = MaterialTheme.typography.labelLarge,
                                color = MutedText,
                            )
                            if (gateway.category.isNotBlank()) {
                                Text(text = gateway.category, style = MaterialTheme.typography.labelLarge, color = MutedText)
                            }
                            Text(
                                text = when (gateway.launchMode) {
                                    GatewayLaunchMode.EXTERNAL_BROWSER -> "Launch: External browser"
                                    GatewayLaunchMode.ISOLATED_IN_APP_VIEWER -> "Launch: Isolated viewer"
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = TerminalGreen,
                            )
                            GatewayUrlValidator.warningFor(gateway.url)?.let {
                                Text(text = it, style = MaterialTheme.typography.labelLarge, color = WarningAmber)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        IconButton(onClick = { pendingLaunch = gateway }) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Launch")
                        }
                        IconButton(onClick = { onEditGateway(gateway.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            scope.launch { gatewayRepository.delete(gateway.id) }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
