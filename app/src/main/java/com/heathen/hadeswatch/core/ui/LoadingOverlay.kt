package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.PanelBorder
import com.heathen.hadeswatch.core.theme.PanelDark
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun HadesTerminalCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PanelDark)
            .border(1.dp, PanelBorder, RoundedCornerShape(8.dp))
            .padding(16.dp),
    ) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = TerminalGreen,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        content()
    }
}

@Composable
fun LoadingOverlay(visible: Boolean, progress: Int = 0) {
    if (!visible) return
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f)),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                text = "Establishing field link…",
                style = MaterialTheme.typography.titleMedium,
                color = TerminalGreen,
            )
            if (progress in 1..99) {
                Text(
                    text = "$progress%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
