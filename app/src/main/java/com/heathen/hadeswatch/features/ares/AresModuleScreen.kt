package com.heathen.hadeswatch.features.ares

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.permissions.PermissionExplainer
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.OracularViolet
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.theme.WarningAmber
import com.heathen.hadeswatch.core.ui.HadesTerminalCard

@Composable
fun AresModuleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "4R3S",
            style = MaterialTheme.typography.displayLarge,
            color = OracularViolet,
        )
        Text(
            text = "Passive signal awareness — coming soon",
            style = MaterialTheme.typography.titleMedium,
            color = WarningAmber,
        )

        HadesTerminalCard(title = "Safety") {
            Text(
                text = "4R3S is a passive, local-first signal awareness tool. It is not for stalking, " +
                    "targeting, tracking private people, evasion, or surveillance. You control when it runs.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        HadesTerminalCard(title = "MVP Status") {
            Text(
                text = "This module does not scan, collect signal data, or run background services in MVP.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "No Bluetooth, Nearby Devices, Location, or Wi-Fi scan permissions are requested.",
                style = MaterialTheme.typography.bodyMedium,
                color = TerminalGreen,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        HadesTerminalCard(title = "Future Permissions") {
            PermissionExplainer.futureAresPermissions.forEach { permission ->
                Text(
                    text = "• $permission",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(vertical = 2.dp),
                )
            }
            Text(
                text = PermissionExplainer.aresFuturePermissionNote(),
                style = MaterialTheme.typography.bodyMedium,
                color = OracularViolet,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
