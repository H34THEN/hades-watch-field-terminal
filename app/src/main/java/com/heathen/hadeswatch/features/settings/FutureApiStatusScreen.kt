package com.heathen.hadeswatch.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.api.MobileApiAvailability
import com.heathen.hadeswatch.core.api.MobileApiNotes
import com.heathen.hadeswatch.core.api.MobileApiStatus
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesTerminalCard

@Composable
fun FutureApiStatusScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = MobileApiNotes.HEADLINE,
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )
        Text(text = MobileApiNotes.SUMMARY, style = MaterialTheme.typography.bodyMedium, color = MutedText)
        Text(text = MobileApiNotes.WEBSITE_TRUTH, style = MaterialTheme.typography.bodyMedium)
        Text(text = MobileApiNotes.LOCAL_TOOLS, style = MaterialTheme.typography.bodyMedium)
        Text(text = MobileApiNotes.CONSENT, style = MaterialTheme.typography.bodyMedium, color = OracularViolet)
        Text(text = MobileApiNotes.NO_POLLING, style = MaterialTheme.typography.bodyMedium, color = MutedText)

        HadesTerminalCard(title = "Planned endpoints (not live)") {
            MobileApiStatus.endpointStatuses.forEach { endpoint ->
                val statusColor = when (endpoint.availability) {
                    MobileApiAvailability.NOT_AVAILABLE -> MutedText
                    MobileApiAvailability.PLANNED -> TerminalGreen
                    MobileApiAvailability.REQUIRES_CONSENT -> WarningAmber
                }
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(
                        text = "${endpoint.method} ${endpoint.route.removePrefix("https://hadeswatch.com")}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TerminalGreen,
                    )
                    Text(text = endpoint.label, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = when (endpoint.availability) {
                            MobileApiAvailability.PLANNED -> "Planned — not called"
                            MobileApiAvailability.REQUIRES_CONSENT -> "Planned — requires opt-in consent"
                            MobileApiAvailability.NOT_AVAILABLE -> "Not available"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = statusColor,
                    )
                }
            }
        }
    }
}
