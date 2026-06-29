package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRouteOption
import com.heathen.hadeswatch.core.navigation.primaryWebRoutes
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesIcon
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.HadesStatusChip
import com.heathen.hadeswatch.core.ui.HadesChipTone
import com.heathen.hadeswatch.core.ui.ToolIconKey
import com.heathen.hadeswatch.features.tools.ToolDefinition
import com.heathen.hadeswatch.features.tools.ToolRegistry
import com.heathen.hadeswatch.features.tools.ToolStatus

data class HudToolAction(
    val title: String,
    val subtitle: String,
    val iconKey: ToolIconKey,
    val route: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HudRouteSelectorSheet(
    visible: Boolean,
    currentUrl: String,
    onDismiss: () -> Unit,
    onSelectRoute: (WebRouteOption) -> Unit,
) {
    if (!visible) return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            item {
                HadesSectionHeader(title = "Hades Watch routes", subtitle = "Allowlisted WebShell only")
            }
            items(primaryWebRoutes) { route ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectRoute(route); onDismiss() }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HadesIcon(key = route.iconKey, contentDescription = route.label, modifier = Modifier.size(28.dp))
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(route.label, color = TerminalGreen, style = MaterialTheme.typography.titleMedium)
                        Text(route.url, color = MutedText, style = MaterialTheme.typography.bodySmall)
                    }
                    if (route.url == currentUrl) {
                        HadesStatusChip(
                            label = "Active",
                            tone = HadesChipTone.AVAILABLE,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HudToolDrawerSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onNavigate: (String) -> Unit,
    k0ReaderEnabled: Boolean = true,
    gatewaysEnabled: Boolean = true,
    signalReaderEnabled: Boolean = true,
    fieldNotesEnabled: Boolean = true,
    aresEnabled: Boolean = true,
) {
    if (!visible) return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tools = ToolRegistry.visibleTools(
        k0ReaderEnabled, gatewaysEnabled, signalReaderEnabled, aresEnabled, fieldNotesEnabled,
    )
    val quickActions = buildList {
        add(HudToolAction("k0R34DER", "Local RSVP reader", ToolIconKey.READER, HadesDestination.Reader.route))
        add(HudToolAction("Signal Reader", "Local snippets", ToolIconKey.SIGNAL, HadesDestination.SignalReader.route))
        add(HudToolAction("Underworld Gateways", "NAS / homelab URLs", ToolIconKey.GATEWAY, HadesDestination.UnderworldGateways.route))
        add(HudToolAction("Field Notes", "Local draft", ToolIconKey.NOTES, HadesDestination.FieldNotes.route))
        add(HudToolAction("Local Tool Data", "Clear local data", ToolIconKey.ARCHIVE, HadesDestination.LocalToolData.route))
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            item {
                HadesSectionHeader(title = "Field Terminal Tools", subtitle = "Local-first overlays")
            }
            items(quickActions) { action ->
                ToolDrawerRow(action) {
                    onNavigate(action.route)
                    onDismiss()
                }
            }
            item {
                HadesSectionHeader(title = "All tools", modifier = Modifier.padding(top = 12.dp))
            }
            items(tools, key = { it.id }) { tool ->
                ToolDefinitionRow(tool) {
                    val route = tool.route ?: tool.webUrl?.let { HadesDestination.webHubRoute(it) }
                        ?: tool.settingsAction
                    route?.let {
                        onNavigate(it)
                        onDismiss()
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolDrawerRow(action: HudToolAction, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HadesIcon(key = action.iconKey, contentDescription = action.title, modifier = Modifier.size(32.dp))
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(action.title, color = TerminalGreen, style = MaterialTheme.typography.titleMedium)
            Text(action.subtitle, color = MutedText, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ToolDefinitionRow(tool: ToolDefinition, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, enabled = tool.status != ToolStatus.Planned)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(tool.name, color = OracularViolet)
            Text(tool.description, color = MutedText, style = MaterialTheme.typography.bodySmall)
        }
        if (tool.status == ToolStatus.ComingSoon) {
            HadesStatusChip(label = "Soon", tone = HadesChipTone.WARNING)
        }
    }
}
