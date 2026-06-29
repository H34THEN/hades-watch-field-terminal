package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun HadesActionCard(
    title: String,
    subtitle: String,
    iconKey: ToolIconKey,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HadesTerminalCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HadesIcon(
                key = iconKey,
                contentDescription = null,
                modifier = Modifier.size(32.dp).padding(end = 12.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TerminalGreen,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
