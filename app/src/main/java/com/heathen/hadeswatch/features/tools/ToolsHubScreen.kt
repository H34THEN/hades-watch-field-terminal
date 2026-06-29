package com.heathen.hadeswatch.features.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.SignalCyan
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
    compactMode: Boolean = false,
) {
    var query by remember { mutableStateOf("") }

    val sections = remember(
        k0ReaderEnabled,
        gatewaysEnabled,
        signalReaderEnabled,
        aresEnabled,
        fieldNotesEnabled,
        query,
    ) {
        ToolRegistry.groupedVisibleTools(
            k0ReaderEnabled = k0ReaderEnabled,
            gatewaysEnabled = gatewaysEnabled,
            signalReaderEnabled = signalReaderEnabled,
            aresEnabled = aresEnabled,
            fieldNotesEnabled = fieldNotesEnabled,
        ).map { (title, tools) ->
            title to if (query.isBlank()) {
                tools
            } else {
                val q = query.lowercase()
                tools.filter { tool ->
                    tool.name.lowercase().contains(q) ||
                        tool.description.lowercase().contains(q) ||
                        tool.category.name.lowercase().contains(q) ||
                        tool.classification.name.lowercase().contains(q)
                }
            }
        }.filter { it.second.isNotEmpty() }
    }

    val cardPadding = if (compactMode) 8.dp else 12.dp

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(if (compactMode) 8.dp else 12.dp),
    ) {
        item {
            Text(
                text = "Tools Hub",
                style = MaterialTheme.typography.displayLarge,
                color = TerminalGreen,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = "Native modules — grouped by trust model and data locality.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = { Text("Search tools") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
            )
        }
        sections.forEach { (sectionTitle, tools) ->
            item {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = sectionColor(sectionTitle),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                )
            }
            items(tools, key = { it.id }) { tool ->
                ToolCard(
                    tool = tool,
                    onNavigate = onNavigate,
                    compactMode = compactMode,
                    cardPadding = cardPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ToolCard(
    tool: ToolDefinition,
    onNavigate: (String) -> Unit,
    compactMode: Boolean,
    cardPadding: androidx.compose.ui.unit.Dp,
) {
    var safetyExpanded by remember { mutableStateOf(false) }

    HadesTerminalCard(title = tool.name) {
        Text(
            text = tool.description,
            style = if (compactMode) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
        )
        FlowRow(
            modifier = Modifier.padding(top = cardPadding),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text(tool.status.name.replace('_', ' ')) },
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = statusColor(tool.status).copy(alpha = 0.15f),
                    disabledLabelColor = statusColor(tool.status),
                ),
            )
            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text(tool.classification.name.replace('_', ' ')) },
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = classificationColor(tool.classification).copy(alpha = 0.15f),
                    disabledLabelColor = classificationColor(tool.classification),
                ),
            )
            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text(tool.category.name) },
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = MutedText.copy(alpha = 0.12f),
                    disabledLabelColor = MutedText,
                ),
            )
        }
        Text(
            text = "Permissions: ${tool.permissionsNeeded}",
            style = MaterialTheme.typography.labelLarge,
            color = MutedText,
            modifier = Modifier.padding(top = cardPadding),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { safetyExpanded = !safetyExpanded }
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Safety",
                style = MaterialTheme.typography.labelLarge,
                color = OracularViolet,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = if (safetyExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (safetyExpanded) "Collapse" else "Expand",
                tint = MutedText,
            )
        }
        AnimatedVisibility(visible = safetyExpanded || !compactMode) {
            Text(
                text = tool.safetyNote,
                style = MaterialTheme.typography.bodyMedium,
                color = OracularViolet,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = cardPadding),
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
private fun sectionColor(title: String) = when {
    title.contains("Web") -> SignalCyan
    title.contains("Gateway") || title.contains("Self-Hosted") -> OracularViolet
    title.contains("Permission") -> WarningAmber
    title.contains("Coming") -> WarningAmber
    else -> TerminalGreen
}

@Composable
private fun statusColor(status: ToolStatus) = when (status) {
    ToolStatus.Available -> TerminalGreen
    ToolStatus.Planned -> SignalCyan
    ToolStatus.ComingSoon -> WarningAmber
    ToolStatus.WebShortcut -> OracularViolet
    ToolStatus.SettingsShortcut -> MutedText
}

@Composable
private fun classificationColor(classification: ToolClassification) = when (classification) {
    ToolClassification.LOCAL_ONLY -> TerminalGreen
    ToolClassification.WEB_SHORTCUT -> SignalCyan
    ToolClassification.FUTURE_API -> OracularViolet
    ToolClassification.PERMISSION_GATED -> WarningAmber
    ToolClassification.COMING_SOON -> WarningAmber
}
