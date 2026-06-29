package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.PanelBorder
import com.heathen.hadeswatch.core.theme.PanelDark
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesIcon
import com.heathen.hadeswatch.core.ui.ToolIconKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandHexMenu(
    visible: Boolean,
    actions: List<CommandHexAction>,
    onDismiss: () -> Unit,
    onAction: (CommandHexAction) -> Unit,
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PanelDark,
        tonalElevation = 0.dp,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 28.dp)
                .sizeIn(maxHeight = 480.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .border(1.dp, PanelBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(PanelDark.copy(alpha = 0.98f))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Field Terminal",
                style = MaterialTheme.typography.titleMedium,
                color = TerminalGreen,
            )
            Text(
                text = "Command menu",
                style = MaterialTheme.typography.bodySmall,
                color = MutedText,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            var lastGroup: CommandHexActionGroup? = null
            actions.forEach { action ->
                if (lastGroup != action.group) {
                    if (lastGroup != null) {
                        HorizontalDivider(
                            color = PanelBorder,
                            modifier = Modifier.padding(vertical = 6.dp),
                        )
                    }
                    Text(
                        text = groupLabel(action.group),
                        style = MaterialTheme.typography.labelMedium,
                        color = SignalCyan,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                    )
                    lastGroup = action.group
                }
                CommandHexMenuRow(
                    action = action,
                    onClick = {
                        if (action.enabled) {
                            onAction(action)
                            onDismiss()
                        }
                    },
                )
            }
        }
    }
}

private fun groupLabel(group: CommandHexActionGroup): String = when (group) {
    CommandHexActionGroup.Navigation -> "Navigate"
    CommandHexActionGroup.WebControls -> "Web shell"
    CommandHexActionGroup.Tools -> "Local tools"
    CommandHexActionGroup.Utility -> "Utility"
}

@Composable
private fun CommandHexMenuRow(
    action: CommandHexAction,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = action.enabled, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        action.iconKey?.let { key ->
            HadesIcon(
                key = key,
                contentDescription = action.label,
                modifier = Modifier.sizeIn(minWidth = 28.dp, minHeight = 28.dp),
            )
        }
        Text(
            text = action.label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (action.enabled) TerminalGreen else MutedText.copy(alpha = 0.5f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}
