package com.heathen.hadeswatch.features.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesTerminalCard

@Composable
fun ToolsHubScreen(
    onNavigate: (String) -> Unit,
    k0ReaderEnabled: Boolean = true,
    gatewaysEnabled: Boolean = true,
    signalReaderEnabled: Boolean = true,
    aresEnabled: Boolean = true,
    fieldNotesEnabled: Boolean = true,
) {
    val sections = ToolRegistry.groupedVisibleTools(
        k0ReaderEnabled = k0ReaderEnabled,
        gatewaysEnabled = gatewaysEnabled,
        signalReaderEnabled = signalReaderEnabled,
        aresEnabled = aresEnabled,
        fieldNotesEnabled = fieldNotesEnabled,
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Tools Hub",
                style = MaterialTheme.typography.displayLarge,
                color = TerminalGreen,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = "Native modules — opt-in, local-first where possible.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
            )
        }
        sections.forEach { (sectionTitle, tools) ->
            item {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = OracularViolet,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                )
            }
            items(tools) { tool ->
                ToolCard(tool = tool, onNavigate = onNavigate)
            }
        }
    }
}

@Composable
private fun ToolCard(tool: ToolDefinition, onNavigate: (String) -> Unit) {
    HadesTerminalCard(title = tool.name) {
        Text(text = tool.description, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "${tool.category.name} · ${tool.classification.name.replace('_', ' ')}",
            style = MaterialTheme.typography.labelLarge,
            color = MutedText,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            text = "Permissions: ${tool.permissionsNeeded}",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = tool.safetyNote,
            style = MaterialTheme.typography.bodyMedium,
            color = OracularViolet,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            text = "Status: ${tool.status.name}",
            style = MaterialTheme.typography.labelLarge,
            color = statusColor(tool.status),
            modifier = Modifier.padding(top = 8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when {
                tool.route != null && tool.status == ToolStatus.Available -> {
                    Button(onClick = { onNavigate(tool.route) }) {
                        Text("Open")
                    }
                }
                tool.route != null && tool.status == ToolStatus.ComingSoon -> {
                    OutlinedButton(onClick = { onNavigate(tool.route) }) {
                        Text("Preview")
                    }
                }
                tool.webUrl != null -> {
                    Button(onClick = {
                        onNavigate(HadesDestination.webRoute(tool.webUrl))
                    }) {
                        Text("Open Web")
                    }
                }
                tool.settingsAction != null -> {
                    Button(onClick = { onNavigate(tool.settingsAction) }) {
                        Text("Settings")
                    }
                }
            }
        }
    }
}

@Composable
private fun statusColor(status: ToolStatus) = when (status) {
    ToolStatus.Available -> TerminalGreen
    ToolStatus.Planned -> com.heathen.hadeswatch.core.theme.SignalCyan
    ToolStatus.ComingSoon -> WarningAmber
    ToolStatus.WebShortcut -> OracularViolet
    ToolStatus.SettingsShortcut -> MutedText
}
