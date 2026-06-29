package com.heathen.hadeswatch.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.features.tools.ToolClassification
import com.heathen.hadeswatch.features.tools.ToolDefinition
import com.heathen.hadeswatch.features.tools.ToolStatus

@Composable
fun HadesToolCard(
    tool: ToolDefinition,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
    openLabel: String = "Open",
) {
    var safetyExpanded by remember { mutableStateOf(false) }
    val statusTone = when (tool.status) {
        ToolStatus.Available -> HadesChipTone.AVAILABLE
        ToolStatus.WebShortcut -> HadesChipTone.WEB
        ToolStatus.ComingSoon -> HadesChipTone.WARNING
        else -> HadesChipTone.NEUTRAL
    }
    val classificationLabel = when (tool.classification) {
        ToolClassification.LOCAL_ONLY -> "Local"
        ToolClassification.WEB_SHORTCUT -> "Web"
        ToolClassification.PERMISSION_GATED -> "Future"
        else -> tool.classification.name.replace('_', ' ')
    }

    HadesTerminalCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            HadesIcon(
                key = toolIconKey(tool.iconKey),
                contentDescription = tool.name,
                modifier = Modifier.size(36.dp).padding(end = 12.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TerminalGreen,
                )
                Text(
                    text = tool.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HadesStatusChip(label = tool.status.name.replace('_', ' '), tone = statusTone)
                    HadesStatusChip(
                        label = classificationLabel,
                        tone = HadesChipTone.INFO,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
                Text(
                    text = "Permissions: ${tool.permissionsNeeded}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MutedText,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Safety details",
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
                AnimatedVisibility(visible = safetyExpanded) {
                    Text(
                        text = tool.safetyNote,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (tool.status == ToolStatus.ComingSoon) WarningAmber else OracularViolet,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    )
                }
                HadesPrimaryButton(
                    text = when {
                        tool.status == ToolStatus.ComingSoon -> "Preview"
                        tool.webUrl != null -> "Open Web"
                        tool.settingsAction != null -> "Settings"
                        else -> openLabel
                    },
                    onClick = onOpen,
                    modifier = Modifier.padding(top = 8.dp),
                    enabled = tool.status != ToolStatus.Planned,
                )
            }
        }
    }
}

private fun toolIconKey(iconKey: String): ToolIconKey = when (iconKey.lowercase()) {
    "k0reader" -> ToolIconKey.READER
    "signal_reader" -> ToolIconKey.SIGNAL
    "gateways" -> ToolIconKey.GATEWAY
    "fieldnotes" -> ToolIconKey.NOTES
    "dead_drops" -> ToolIconKey.DEAD_DROP
    "ares" -> ToolIconKey.ARES
    "accessibility" -> ToolIconKey.ACCESSIBILITY
    else -> ToolIconKey.TERMINAL
}
