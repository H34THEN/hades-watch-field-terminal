package com.heathen.hadeswatch.features.signalreader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import com.heathen.hadeswatch.features.k0reader.ReaderTransferRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SignalReaderDetailScreen(
    repository: SignalSnippetRepository,
    snippetId: String,
    onEdit: () -> Unit,
    onReadInK0Reader: () -> Unit,
    onDeleted: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    var snippet by remember { mutableStateOf<SignalSnippet?>(null) }

    LaunchedEffect(snippetId) {
        snippet = repository.snippets.first().find { it.id == snippetId }
    }

    val s = snippet ?: run {
        Text("Snippet not found.", modifier = Modifier.padding(16.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = s.title.ifBlank { "Untitled signal" },
            style = MaterialTheme.typography.displayLarge,
            color = SignalCyan,
        )
        if (s.sourceLabel.isNotBlank()) {
            Text("Source: ${s.sourceLabel}", style = MaterialTheme.typography.bodyMedium, color = TerminalGreen)
        }
        if (s.tags.isNotBlank()) {
            Text("Tags: ${s.tags}", style = MaterialTheme.typography.bodyMedium, color = MutedText)
        }
        HadesTerminalCard(title = "Transmission") {
            Text(text = s.body, style = MaterialTheme.typography.bodyLarge)
        }
        Text(
            text = "Read in k0R34DER transfers text locally only — nothing is uploaded.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                ReaderTransferRepository.setPending(
                    text = s.body,
                    sourceTitle = s.title.ifBlank { "Untitled signal" },
                )
                onReadInK0Reader()
            }) {
                Text("Read in k0R34DER")
            }
            OutlinedButton(onClick = {
                clipboard.setText(AnnotatedString(s.body))
            }) {
                Text("Copy")
            }
            OutlinedButton(onClick = onEdit) {
                Text("Edit")
            }
        }
        OutlinedButton(
            onClick = {
                scope.launch {
                    repository.delete(s.id)
                    onDeleted()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Delete snippet")
        }
    }
}
