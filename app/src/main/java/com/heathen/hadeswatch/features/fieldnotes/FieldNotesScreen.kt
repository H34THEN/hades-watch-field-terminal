package com.heathen.hadeswatch.features.fieldnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun FieldNotesScreen(settingsRepository: AppSettingsRepository) {
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    var draft by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        draft = settingsRepository.fieldNotesDraft.first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Field Notes",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )
        Text(
            text = "Local draft only. Copy into hadeswatch.com manually. No sync in MVP.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )

        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            label = { Text("Draft") },
            minLines = 12,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                scope.launch {
                    settingsRepository.setFieldNotesDraft(draft)
                    savedMessage = "Draft saved locally."
                }
            }) {
                Text("Save draft")
            }
            OutlinedButton(onClick = {
                clipboard.setText(AnnotatedString(draft))
                savedMessage = "Copied to clipboard."
            }) {
                Text("Copy")
            }
            OutlinedButton(onClick = {
                draft = ""
                scope.launch {
                    settingsRepository.setFieldNotesDraft("")
                    savedMessage = "Draft cleared."
                }
            }) {
                Text("Clear")
            }
        }

        savedMessage?.let {
            Text(text = it, style = MaterialTheme.typography.bodyMedium, color = MutedText)
        }
    }
}
