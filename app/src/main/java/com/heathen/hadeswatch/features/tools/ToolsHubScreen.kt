package com.heathen.hadeswatch.features.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesEmptyState
import com.heathen.hadeswatch.core.ui.HadesSearchBar
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.HadesToolCard

@Composable
fun ToolsHubScreen(
    onNavigate: (String) -> Unit,
    k0ReaderEnabled: Boolean = true,
    gatewaysEnabled: Boolean = true,
    signalReaderEnabled: Boolean = true,
    aresEnabled: Boolean = true,
    fieldNotesEnabled: Boolean = true,
) {
    var query by remember { mutableStateOf("") }

    val sections = remember(
        k0ReaderEnabled,
        gatewaysEnabled,
        signalReaderEnabled,
        aresEnabled,
        fieldNotesEnabled,
        query,
    ) {
        ToolRegistry.groupedVisibleTools(
            k0ReaderEnabled = k0ReaderEnabled,
            gatewaysEnabled = gatewaysEnabled,
            signalReaderEnabled = signalReaderEnabled,
            aresEnabled = aresEnabled,
            fieldNotesEnabled = fieldNotesEnabled,
        ).map { (title, tools) ->
            title to if (query.isBlank()) {
                tools
            } else {
                val q = query.lowercase()
                tools.filter { tool ->
                    tool.name.lowercase().contains(q) || tool.description.lowercase().contains(q)
                }
            }
        }.filter { it.second.isNotEmpty() }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Tools",
                style = MaterialTheme.typography.headlineMedium,
                color = TerminalGreen,
            )
            Text(
                text = "Local-first native modules for the Field Terminal.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            HadesSearchBar(
                value = query,
                onValueChange = { query = it },
                placeholder = "Search tools",
            )
        }

        if (sections.isEmpty()) {
            item {
                HadesEmptyState(
                    title = "No tools found",
                    message = "Try a different search term or enable tools in Settings.",
                )
            }
        }

        sections.forEach { (sectionTitle, tools) ->
            item {
                HadesSectionHeader(title = sectionTitle)
            }
            items(tools, key = { it.id }) { tool ->
                HadesToolCard(
                    tool = tool,
                    onOpen = {
                        when {
                            tool.route != null -> onNavigate(tool.route)
                            tool.webUrl != null -> onNavigate(HadesDestination.webRoute(tool.webUrl))
                            tool.settingsAction != null -> onNavigate(tool.settingsAction)
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }
    }
}
