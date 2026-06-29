package com.heathen.hadeswatch.features.gateways

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import kotlinx.coroutines.launch

@Composable
fun UnderworldGatewaysScreen(
    gatewayRepository: GatewayRepository,
    onAddGateway: () -> Unit,
    onEditGateway: (String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gateways by gatewayRepository.gateways.collectAsState(initial = emptyList())
    var pendingHttpUrl by remember { mutableStateOf<GatewayDefinition?>(null) }

    pendingHttpUrl?.let { gateway ->
        AlertDialog(
            onDismissRequest = { pendingHttpUrl = null },
            title = { Text("Non-HTTPS gateway") },
            text = {
                Text(
                    "This URL uses HTTP (common on local LAN). It will open outside the " +
                        "Hades Watch trusted WebView and will not share Hades Watch cookies.\n\n${gateway.url}",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    GatewayLauncher.launch(context, gateway.url)
                    pendingHttpUrl = null
                }) {
                    Text("Open anyway")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingHttpUrl = null }) {
                    Text("Cancel")
                }
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
                Text(
                    text = "Underworld Gateways",
                    style = MaterialTheme.typography.displayLarge,
                    color = OracularViolet,
                )
                Text(
                    text = "Local NAS & homelab launch panel — separate from Hades Watch WebView.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            if (gateways.isEmpty()) {
                item {
                    HadesTerminalCard(title = "No gateways") {
                        Text(
                            text = "Add Jellyfin, Home Assistant, NAS dashboards, or other trusted local URLs.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            items(gateways, key = { it.id }) { gateway ->
                HadesTerminalCard(title = gateway.displayName) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = gateway.icon.imageVector(),
                            contentDescription = gateway.icon.label,
                            tint = TerminalGreen,
                        )
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(text = gateway.url, style = MaterialTheme.typography.bodyMedium)
                            if (gateway.category.isNotBlank()) {
                                Text(
                                    text = gateway.category,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MutedText,
                                )
                            }
                            if (gateway.note.isNotBlank()) {
                                Text(
                                    text = gateway.note,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedText,
                                )
                            }
                            if (!GatewayUrlValidator.isHttps(gateway.url)) {
                                Text(
                                    text = "HTTP — local/LAN URL",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = WarningAmber,
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        IconButton(onClick = {
                            if (GatewayUrlValidator.isHttps(gateway.url)) {
                                GatewayLauncher.launch(context, gateway.url)
                            } else {
                                pendingHttpUrl = gateway
                            }
                        }) {
                            Icon(Icons.Default.OpenInNew, contentDescription = "Launch")
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
