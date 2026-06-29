package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.PanelDark
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun HudStatusChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showShield: Boolean = true,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(PanelDark.copy(alpha = 0.92f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .heightIn(min = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (showShield) {
            Icon(Icons.Default.Shield, contentDescription = "Trusted shell", tint = TerminalGreen)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = TerminalGreen,
        )
    }
}

@Composable
fun HudPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PanelDark.copy(alpha = 0.94f))
            .padding(12.dp),
    ) {
        content()
    }
}

@Composable
fun HudOverlayContainer(
    modifier: Modifier = Modifier,
    topOverlay: @Composable () -> Unit = {},
    bottomOverlay: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(8.dp),
        ) {
            topOverlay()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            bottomOverlay()
        }
    }
}
