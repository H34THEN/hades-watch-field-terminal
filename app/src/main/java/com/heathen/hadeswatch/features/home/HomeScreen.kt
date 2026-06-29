package com.heathen.hadeswatch.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.navigation.HadesDestination
import com.heathen.hadeswatch.core.navigation.WebRouteOption
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.core.navigation.primaryWebRoutes
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesActionCard
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import com.heathen.hadeswatch.core.ui.HadesWarningBox
import com.heathen.hadeswatch.core.ui.ToolIconKey

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onNavigateToWebRoute: (String) -> Unit = { onNavigate(HadesDestination.webHubRoute(it)) },
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "Hades Watch Field Terminal",
                style = MaterialTheme.typography.headlineMedium,
                color = SignalCyan,
            )
            Text(
                text = "Mobile Underwatch companion",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedText,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )
        }

        item {
            HadesSectionHeader(title = "Primary actions")
            HadesActionCard(
                title = "Open Hades Watch",
                subtitle = "Dashboard and website routes",
                iconKey = ToolIconKey.WEB,
                onClick = { onNavigate(HadesDestination.webHubRoute()) },
            )
            HadesActionCard(
                title = "Continue Reading",
                subtitle = "k0R34DER local speed reader",
                iconKey = ToolIconKey.READER,
                onClick = { onNavigate(HadesDestination.Reader.route) },
                modifier = Modifier.padding(top = 8.dp),
            )
            HadesActionCard(
                title = "Open Signal Reader",
                subtitle = "Local snippet library",
                iconKey = ToolIconKey.SIGNAL,
                onClick = { onNavigate(HadesDestination.SignalReader.route) },
                modifier = Modifier.padding(top = 8.dp),
            )
            HadesActionCard(
                title = "Open Underworld Gateways",
                subtitle = "NAS and homelab URLs",
                iconKey = ToolIconKey.GATEWAY,
                onClick = { onNavigate(HadesDestination.UnderworldGateways.route) },
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        item {
            HadesSectionHeader(title = "Quick web routes", subtitle = "Opens in Web tab")
        }
        items(primaryWebRoutes) { route ->
            WebRouteActionCard(route = route, onClick = { onNavigateToWebRoute(route.url) })
        }

        item {
            HadesSectionHeader(title = "Local tools")
        }
        item {
            HadesActionCard(
                title = "k0R34DER",
                subtitle = "RSVP speed reader",
                iconKey = ToolIconKey.READER,
                onClick = { onNavigate(HadesDestination.Reader.route) },
            )
        }
        item {
            HadesActionCard(
                title = "Signal Reader",
                subtitle = "Saved snippets",
                iconKey = ToolIconKey.SIGNAL,
                onClick = { onNavigate(HadesDestination.SignalReader.route) },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        item {
            HadesActionCard(
                title = "Underworld Gateways",
                subtitle = "Self-hosted URLs",
                iconKey = ToolIconKey.GATEWAY,
                onClick = { onNavigate(HadesDestination.UnderworldGateways.route) },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        item {
            HadesActionCard(
                title = "Field Notes",
                subtitle = "Local draft",
                iconKey = ToolIconKey.NOTES,
                onClick = { onNavigate(HadesDestination.FieldNotes.route) },
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        item {
            HadesTerminalCard(title = "Status", modifier = Modifier.padding(top = 8.dp)) {
                Text("Website login handled by hadeswatch.com", color = TerminalGreen)
                Text("Native tools are local-first", color = MutedText, modifier = Modifier.padding(top = 4.dp))
                Text("No scanning enabled", color = MutedText, modifier = Modifier.padding(top = 4.dp))
            }
            HadesWarningBox(
                message = "Underworld Gateways are separate from Hades Watch session.",
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            )
        }
    }
}

@Composable
private fun WebRouteActionCard(route: WebRouteOption, onClick: () -> Unit) {
    HadesActionCard(
        title = route.label,
        subtitle = route.url.removePrefix(WebRoutes.BASE),
        iconKey = route.iconKey,
        onClick = onClick,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}
