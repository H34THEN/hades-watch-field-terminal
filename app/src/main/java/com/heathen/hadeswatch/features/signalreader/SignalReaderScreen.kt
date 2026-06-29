package com.heathen.hadeswatch.features.signalreader

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesEmptyState
import com.heathen.hadeswatch.core.ui.HadesIcon
import com.heathen.hadeswatch.core.ui.HadesSearchBar
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import com.heathen.hadeswatch.core.ui.ToolIconKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SignalReaderScreen(
    repository: SignalSnippetRepository,
    onAddSnippet: () -> Unit,
    onOpenSnippet: (String) -> Unit,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val snippets by repository.snippets.collectAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }
    var showImportDialog by remember { mutableStateOf(false) }
    var importJson by remember { mutableStateOf("") }
    var importPreview by remember { mutableStateOf<SignalSnippetStorage.ImportPreview?>(null) }
    var importResult by remember { mutableStateOf<SignalSnippetStorage.ImportResult?>(null) }

    importPreview?.let { preview ->
        AlertDialog(
            onDismissRequest = { importPreview = null },
            title = { Text("Import snippets") },
            text = {
                Column {
                    Text(
                        "Found ${preview.total} entries (${preview.validCount} valid, " +
                            "${preview.invalidCount} invalid).",
                    )
                    Text(
                        "Merge without overwriting existing IDs. Duplicates are skipped.",
                        color = MutedText,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    if (preview.snippets.isNotEmpty()) {
                        Text(
                            text = preview.snippets.take(5).joinToString("\n") { s ->
                                "• ${s.title.ifBlank { "Untitled" }}"
                            } + if (preview.snippets.size > 5) "\n…" else "",
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        val existingIds = repository.snippets.first().map { it.id }.toSet()
                        val result = repository.mergeImport(
                            incoming = preview.snippets,
                            existingIds = existingIds,
                            overwrite = false,
                            invalidCount = preview.invalidCount,
                        )
                        importResult = result
                        importPreview = null
                        showImportDialog = false
                        importJson = ""
                    }
                }) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { importPreview = null }) { Text("Cancel") }
            },
        )
    }

    importResult?.let { result ->
        AlertDialog(
            onDismissRequest = { importResult = null },
            title = { Text("Import complete") },
            text = {
                Text(
                    "Added: ${result.added}\n" +
                        "Skipped: ${result.skipped}\n" +
                        "Duplicates: ${result.duplicate}\n" +
                        "Invalid: ${result.invalid}",
                )
            },
            confirmButton = {
                TextButton(onClick = { importResult = null }) { Text("OK") }
            },
        )
    }

    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Paste snippet JSON") },
            text = {
                Column {
                    Text("Paste exported JSON. Title and body must not be blank.", color = MutedText)
                    OutlinedTextField(
                        value = importJson,
                        onValueChange = { importJson = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        minLines = 4,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    importPreview = SignalSnippetStorage.validateImportJson(importJson)
                }) {
                    Text("Preview")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) { Text("Cancel") }
            },
        )
    }

    val filtered = remember(snippets, query) {
        if (query.isBlank()) snippets
        else {
            val q = query.lowercase()
            snippets.filter { s ->
                s.title.lowercase().contains(q) ||
                    s.body.lowercase().contains(q) ||
                    s.tags.lowercase().contains(q) ||
                    s.sourceLabel.lowercase().contains(q)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSnippet) {
                Icon(Icons.Default.Add, contentDescription = "Add snippet")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    HadesIcon(key = ToolIconKey.SIGNAL, contentDescription = "Signal Reader")
                    Text(
                        text = "Signal Reader",
                        style = MaterialTheme.typography.headlineMedium,
                        color = SignalCyan,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
                Text(
                    text = "Local snippet library — manually saved text only.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = {
                        val json = repository.exportJson(snippets)
                        clipboard.setText(AnnotatedString(json))
                    }) {
                        Icon(Icons.Default.FileDownload, contentDescription = null)
                        Text("Copy export", modifier = Modifier.padding(start = 4.dp))
                    }
                    OutlinedButton(onClick = {
                        val json = repository.exportJson(snippets)
                        context.startActivity(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "application/json"
                                putExtra(Intent.EXTRA_TEXT, json)
                                putExtra(Intent.EXTRA_SUBJECT, "Signal Reader backup")
                            },
                        )
                    }) {
                        Text("Share JSON")
                    }
                    OutlinedButton(onClick = { showImportDialog = true }) {
                        Icon(Icons.Default.FileUpload, contentDescription = null)
                        Text("Import", modifier = Modifier.padding(start = 4.dp))
                    }
                }
                HadesSearchBar(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Search snippets",
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
            if (filtered.isEmpty()) {
                item {
                    HadesEmptyState(
                        title = "No snippets",
                        message = "Tap + to save lore, transmissions, or copied text locally.",
                        action = {
                            com.heathen.hadeswatch.core.ui.HadesPrimaryButton(
                                text = "Add snippet",
                                onClick = onAddSnippet,
                            )
                        },
                    )
                }
            }
            items(filtered, key = { it.id }) { snippet ->
                HadesTerminalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenSnippet(snippet.id) },
                    title = snippet.title.ifBlank { "Untitled signal" },
                ) {
                    Text(
                        text = snippet.body.lineSequence().firstOrNull()?.take(120) ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText,
                        maxLines = 2,
                    )
                    if (snippet.sourceLabel.isNotBlank()) {
                        Text(
                            text = "Source: ${snippet.sourceLabel}",
                            style = MaterialTheme.typography.labelLarge,
                            color = TerminalGreen,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                    if (snippet.tags.isNotBlank()) {
                        Text(
                            text = "Tags: ${snippet.tags}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MutedText,
                        )
                    }
                }
            }
        }
    }
}
