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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.PanelDark
import com.heathen.hadeswatch.core.theme.PanelBorder
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesIcon
import com.heathen.hadeswatch.core.ui.ToolIconKey
import kotlin.math.roundToInt

@Composable
fun CommandHexMenu(
    visible: Boolean,
    hexCenterX: Float,
    hexCenterY: Float,
    hexSizePx: Float,
    screenWidthPx: Float,
    screenHeightPx: Float,
    actions: List<CommandHexAction>,
    onDismiss: () -> Unit,
    onAction: (CommandHexAction) -> Unit,
) {
    if (!visible) return

    val menuWidthDp = 280.dp
    val density = LocalDensity.current
    val menuWidthPx = with(density) { menuWidthDp.toPx() }
    val menuMaxHeightPx = screenHeightPx * 0.55f

    val preferLeft = hexCenterX > screenWidthPx / 2f
    val preferAbove = hexCenterY > screenHeightPx * 0.55f

    val offsetX = when {
        preferLeft -> (hexCenterX - menuWidthPx - hexSizePx / 2f).coerceAtLeast(8f)
        else -> (hexCenterX + hexSizePx / 2f).coerceAtMost(screenWidthPx - menuWidthPx - 8f)
    }
    val offsetY = when {
        preferAbove -> (hexCenterY - menuMaxHeightPx - hexSizePx / 2f).coerceAtLeast(8f)
        else -> (hexCenterY + hexSizePx / 2f).coerceAtMost(screenHeightPx - menuMaxHeightPx - 8f)
    }

    Popup(
        alignment = androidx.compose.ui.Alignment.TopStart,
        offset = IntOffset(offsetX.roundToInt(), offsetY.roundToInt()),
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = menuWidthDp)
                .sizeIn(maxHeight = with(density) { menuMaxHeightPx.toDp() })
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(PanelDark.copy(alpha = 0.97f))
                .border(1.dp, PanelBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Field Terminal",
                style = MaterialTheme.typography.titleMedium,
                color = TerminalGreen,
            )
            Text(
                text = "Command Hex menu",
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
                            modifier = Modifier.padding(vertical = 4.dp),
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
            .padding(horizontal = 8.dp, vertical = 10.dp),
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
        )
    }
}
