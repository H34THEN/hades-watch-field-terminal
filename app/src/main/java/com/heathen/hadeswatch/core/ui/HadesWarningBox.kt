package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.WarningAmber

@Composable
fun HadesWarningBox(
    message: String,
    modifier: Modifier = Modifier,
) {
    HadesTerminalCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            HadesIcon(
                key = ToolIconKey.WARNING,
                contentDescription = "Warning",
                modifier = Modifier.padding(end = 12.dp),
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = WarningAmber,
            )
        }
    }
}
