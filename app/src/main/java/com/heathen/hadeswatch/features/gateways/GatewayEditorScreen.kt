package com.heathen.hadeswatch.features.gateways

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.PomegranateRed
import com.heathen.hadeswatch.core.theme.TerminalGreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GatewayEditorScreen(
    gatewayRepository: GatewayRepository,
    gatewayId: String?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var displayName by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(GatewayIcon.CUSTOM) }
    var customIconUri by remember { mutableStateOf<String?>(null) }
    var launchMode by remember { mutableStateOf(GatewayLaunchMode.EXTERNAL_BROWSER) }
    var sortOrder by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri != null) {
            customIconUri = uri.toString()
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            }
        }
    }

    LaunchedEffect(gatewayId) {
        if (gatewayId != null) {
            gatewayRepository.gateways.first().find { it.id == gatewayId }?.let { existing ->
                displayName = existing.displayName
                url = existing.url
                category = existing.category
                note = existing.note
                selectedIcon = existing.icon
                customIconUri = existing.customIconUri
                launchMode = existing.launchMode
                sortOrder = existing.sortOrder
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (gatewayId == null) "Add Gateway" else "Edit Gateway",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )
        Text(
            text = "Gateway URLs are user-defined and separate from Hades Watch trusted WebView.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Display name") },
            singleLine = true,
        )
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("URL (http:// or https://)") },
            singleLine = true,
        )
        GatewayUrlValidator.warningFor(url)?.let { warning ->
            Text(text = warning, style = MaterialTheme.typography.bodyMedium, color = PomegranateRed)
        }
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category (optional)") },
            singleLine = true,
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Note (optional)") },
            minLines = 2,
        )
        OutlinedTextField(
            value = sortOrder.toString(),
            onValueChange = { sortOrder = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Sort order (lower first)") },
            singleLine = true,
        )

        Text(text = "Launch mode", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GatewayLaunchMode.entries.forEach { mode ->
                FilterChip(
                    selected = launchMode == mode,
                    onClick = { launchMode = mode },
                    label = {
                        Text(
                            when (mode) {
                                GatewayLaunchMode.EXTERNAL_BROWSER -> "External browser"
                                GatewayLaunchMode.ISOLATED_IN_APP_VIEWER -> "In-app viewer"
                            },
                        )
                    },
                )
            }
        }

        Text(text = "Built-in icon", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GatewayIcon.entries.forEach { icon ->
                FilterChip(
                    selected = selectedIcon == icon,
                    onClick = { selectedIcon = icon },
                    label = { Text(icon.label) },
                    leadingIcon = { Icon(icon.imageVector(), contentDescription = icon.label) },
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(onClick = {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text("Choose custom icon")
            }
            if (customIconUri != null) {
                Text("Custom icon set", style = MaterialTheme.typography.bodyMedium, color = TerminalGreen)
                OutlinedButton(onClick = { customIconUri = null }) {
                    Text("Clear")
                }
            }
        }
        Text(
            text = "Uses system photo picker — no storage permission required.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )

        error?.let { Text(text = it, color = PomegranateRed) }

        Button(
            onClick = {
                val normalizedUrl = GatewayUrlValidator.normalize(url)
                if (displayName.isBlank()) {
                    error = "Display name is required."
                    return@Button
                }
                if (!GatewayUrlValidator.isValid(normalizedUrl)) {
                    error = "URL must start with http:// or https://"
                    return@Button
                }
                error = null
                val gateway = GatewayDefinition(
                    id = gatewayId ?: java.util.UUID.randomUUID().toString(),
                    displayName = displayName.trim(),
                    url = normalizedUrl,
                    iconKey = selectedIcon.name,
                    customIconUri = customIconUri,
                    category = category.trim(),
                    note = note.trim(),
                    launchMode = launchMode,
                    sortOrder = sortOrder,
                )
                scope.launch {
                    gatewayRepository.upsert(gateway)
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save gateway")
        }
        Button(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel")
        }
    }
}
