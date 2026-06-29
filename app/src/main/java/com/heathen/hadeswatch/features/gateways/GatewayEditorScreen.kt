package com.heathen.hadeswatch.features.gateways

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    val scope = rememberCoroutineScope()
    var displayName by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(GatewayIcon.CUSTOM) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(gatewayId) {
        if (gatewayId != null) {
            val existing = gatewayRepository.gateways.first().find { it.id == gatewayId }
            if (existing != null) {
                displayName = existing.displayName
                url = existing.url
                category = existing.category
                note = existing.note
                selectedIcon = existing.icon
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
            text = "Gateway URLs open externally and do not use Hades Watch session cookies.",
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

        Text(text = "Icon", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GatewayIcon.entries.forEach { icon ->
                FilterChip(
                    selected = selectedIcon == icon,
                    onClick = { selectedIcon = icon },
                    label = { Text(icon.label) },
                    leadingIcon = {
                        Icon(icon.imageVector(), contentDescription = icon.label)
                    },
                )
            }
        }

        error?.let {
            Text(text = it, color = PomegranateRed, style = MaterialTheme.typography.bodyMedium)
        }

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
                    icon = selectedIcon,
                    category = category.trim(),
                    note = note.trim(),
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
