package com.heathen.hadeswatch.features.signalreader

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesTerminalCard

@Composable
fun SignalReaderScreen(
    repository: SignalSnippetRepository,
    onAddSnippet: () -> Unit,
    onOpenSnippet: (String) -> Unit,
) {
    val snippets by repository.snippets.collectAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }

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
                Text(
                    text = "Signal Reader",
                    style = MaterialTheme.typography.displayLarge,
                    color = SignalCyan,
                )
                Text(
                    text = "Local snippet library — manually saved text only. No scraping.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    label = { Text("Search snippets") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                )
            }
            if (filtered.isEmpty()) {
                item {
                    HadesTerminalCard(title = "No snippets") {
                        Text(
                            text = "Save lore, transmissions, or copied text you want to keep locally.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
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
