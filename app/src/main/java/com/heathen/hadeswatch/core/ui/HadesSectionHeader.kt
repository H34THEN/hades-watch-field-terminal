package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun HadesSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = TerminalGreen,
        modifier = modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
    )
    subtitle?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )
    }
}
