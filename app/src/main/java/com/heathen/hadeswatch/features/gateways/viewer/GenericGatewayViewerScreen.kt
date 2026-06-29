package com.heathen.hadeswatch.features.gateways.viewer

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.OfflineErrorView
import com.heathen.hadeswatch.features.gateways.GatewayDefinition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericGatewayViewerScreen(
    gateway: GatewayDefinition,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var showHttpWarning by remember {
        mutableStateOf(!GatewayViewerPolicy.isHttps(gateway.url))
    }
    var confirmedHttp by remember { mutableStateOf(false) }

    if (showHttpWarning && !confirmedHttp) {
        AlertDialog(
            onDismissRequest = onClose,
            title = { Text("Gateway Viewer — HTTP") },
            text = {
                Text(
                    "This gateway uses HTTP. Content is not encrypted. " +
                        "This viewer is isolated from Hades Watch and does not share Hades Watch cookies.\n\n" +
                        gateway.url,
                )
            },
            confirmButton = {
                TextButton(onClick = { confirmedHttp = true; showHttpWarning = false }) {
                    Text("Continue in viewer")
                }
            },
            dismissButton = {
                TextButton(onClick = onClose) { Text("Close") }
            },
        )
        return
    }

    BackHandler(onBack = onClose)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Gateway Viewer",
                        style = MaterialTheme.typography.labelLarge,
                        color = OracularViolet,
                    )
                    Text(
                        text = gateway.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                    )
                    Text(
                        text = GatewayViewerPolicy.hostLabel(gateway.url),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText,
                        maxLines = 1,
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close viewer")
                }
            },
            actions = {
                IconButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(gateway.url)))
                }) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open in browser")
                }
            },
        )
        if (isLoading && progress in 1..99) {
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Text(
            text = "Not Hades Watch — user gateway only",
            style = MaterialTheme.typography.labelLarge,
            color = WarningAmber,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Box(modifier = Modifier.fillMaxSize()) {
            if (hasError) {
                OfflineErrorView(
                    message = "Could not load gateway. Try external browser.",
                    onRetry = { hasError = false; isLoading = true },
                )
            } else {
                GenericGatewayWebView(
                    url = gateway.url,
                    modifier = Modifier.fillMaxSize(),
                    openExternalUrl = { target ->
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
                    },
                    onLoadingChanged = { isLoading = it },
                    onProgressChanged = { progress = it },
                    onError = { hasError = true },
                )
            }
        }
    }
}
