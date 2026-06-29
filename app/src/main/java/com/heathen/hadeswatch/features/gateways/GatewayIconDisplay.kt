package com.heathen.hadeswatch.features.gateways

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.TerminalGreen

@Composable
fun GatewayIconDisplay(
    gateway: GatewayDefinition,
    modifier: Modifier = Modifier,
    contentDescription: String = gateway.displayName,
) {
    val context = LocalContext.current
    val bitmap = remember(gateway.customIconUri) {
        gateway.customIconUri?.let { uriString ->
            runCatching {
                context.contentResolver.openInputStream(Uri.parse(uriString))?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            }.getOrNull()
        }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier.size(40.dp),
        )
    } else {
        Icon(
            imageVector = gateway.icon.imageVector(),
            contentDescription = contentDescription,
            tint = TerminalGreen,
            modifier = modifier.size(40.dp),
        )
    }
}
