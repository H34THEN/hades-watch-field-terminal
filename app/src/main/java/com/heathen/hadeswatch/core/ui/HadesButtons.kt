package com.heathen.hadeswatch.core.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HadesPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
    ) {
        Text(text)
    }
}

@Composable
fun HadesSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
    ) {
        Text(text)
    }
}

@Composable
fun HadesPrimaryButtonFull(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HadesPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    )
}
