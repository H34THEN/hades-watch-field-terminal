package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber

@Composable
fun HadesStatusChip(
    label: String,
    tone: HadesChipTone = HadesChipTone.NEUTRAL,
    modifier: Modifier = Modifier,
) {
    val (container, text) = when (tone) {
        HadesChipTone.AVAILABLE -> TerminalGreen.copy(alpha = 0.15f) to TerminalGreen
        HadesChipTone.WEB -> SignalCyan.copy(alpha = 0.15f) to SignalCyan
        HadesChipTone.WARNING -> WarningAmber.copy(alpha = 0.15f) to WarningAmber
        HadesChipTone.INFO -> OracularViolet.copy(alpha = 0.15f) to OracularViolet
        HadesChipTone.NEUTRAL -> MutedText.copy(alpha = 0.12f) to MutedText
    }
    AssistChip(
        onClick = {},
        enabled = false,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 2.dp),
            )
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            disabledContainerColor = container,
            disabledLabelColor = text,
        ),
    )
}

enum class HadesChipTone {
    AVAILABLE,
    WEB,
    WARNING,
    INFO,
    NEUTRAL,
}
