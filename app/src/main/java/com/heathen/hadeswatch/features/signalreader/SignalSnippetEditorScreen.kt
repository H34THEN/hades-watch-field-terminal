package com.heathen.hadeswatch.features.signalreader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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

@Composable
fun SignalSnippetEditorScreen(
    repository: SignalSnippetRepository,
    snippetId: String?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var sourceLabel by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(snippetId) {
        if (snippetId != null) {
            repository.snippets.first().find { it.id == snippetId }?.let { s ->
                title = s.title
                body = s.body
                sourceLabel = s.sourceLabel
                tags = s.tags
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
            text = if (snippetId == null) "Add Signal" else "Edit Signal",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )
        Text(
            text = "Paste text you saved manually. This tool does not scrape Hades Watch.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Title") },
            singleLine = true,
        )
        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Body") },
            minLines = 8,
        )
        OutlinedTextField(
            value = sourceLabel,
            onValueChange = { sourceLabel = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Source label (optional)") },
            singleLine = true,
        )
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Tags (comma-separated)") },
            singleLine = true,
        )
        error?.let { Text(it, color = PomegranateRed) }
        Button(
            onClick = {
                if (body.isBlank()) {
                    error = "Body is required."
                    return@Button
                }
                error = null
                val snippet = SignalSnippet(
                    id = snippetId ?: java.util.UUID.randomUUID().toString(),
                    title = title.trim(),
                    body = body,
                    sourceLabel = sourceLabel.trim(),
                    tags = tags.trim(),
                )
                scope.launch {
                    repository.upsert(snippet)
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save snippet")
        }
        Button(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel")
        }
    }
}
