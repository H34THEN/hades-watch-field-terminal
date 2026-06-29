package com.heathen.hadeswatch.features.k0reader

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun K0ReaderScreen(
    settingsRepository: AppSettingsRepository,
    reducedMotion: Boolean = false,
) {
    val scope = rememberCoroutineScope()
    val adapter = rememberK0ReaderAdapter()
    val engine = adapter.engineRef()
    val prefs = remember { K0ReaderPreferences(settingsRepository) }

    var inputText by remember { mutableStateOf("") }
    var displayToken by remember { mutableStateOf("") }
    var isPlaying by remember { mutableStateOf(false) }
    var wpm by remember { mutableIntStateOf(300) }
    var chunkSize by remember { mutableIntStateOf(1) }
    var fontSize by remember { mutableIntStateOf(32) }

    LaunchedEffect(Unit) {
        wpm = prefs.wpm.first()
        chunkSize = prefs.chunkSize.first()
        fontSize = prefs.fontSize.first()
    }

    LaunchedEffect(isPlaying, wpm, reducedMotion) {
        if (!isPlaying || reducedMotion) return@LaunchedEffect
        while (isActive && isPlaying) {
            delay(engine.intervalMillis())
            val next = adapter.nextToken()
            if (next == null) {
                isPlaying = false
            } else {
                displayToken = next
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
            text = "k0R34DER",
            style = MaterialTheme.typography.displayLarge,
            color = TerminalGreen,
        )
        Text(
            text = "Local RSVP reader — paste text, no upload.",
            style = MaterialTheme.typography.bodyMedium,
        )

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Paste text") },
            minLines = 4,
        )

        HadesTerminalCard(title = "Display") {
            Text(
                text = displayToken.ifBlank { "—" },
                style = MaterialTheme.typography.displayLarge.copy(fontSize = fontSize.sp),
                color = SignalCyan,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
            Text(
                text = "${adapter.currentIndex() + 1} / ${adapter.tokenCount()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }

        Text("WPM: $wpm")
        Slider(
            value = wpm.toFloat(),
            onValueChange = {
                wpm = it.toInt()
                adapter.setWordsPerMinute(wpm)
                scope.launch { prefs.saveWpm(wpm) }
            },
            valueRange = 100f..800f,
            steps = 13,
        )

        Text("Chunk size: $chunkSize word(s)")
        Slider(
            value = chunkSize.toFloat(),
            onValueChange = {
                chunkSize = it.toInt()
                adapter.setChunkSize(chunkSize)
                scope.launch { prefs.saveChunkSize(chunkSize) }
            },
            valueRange = 1f..4f,
            steps = 2,
        )

        Text("Font size: $fontSize")
        Slider(
            value = fontSize.toFloat(),
            onValueChange = {
                fontSize = it.toInt()
                scope.launch { prefs.saveFontSize(fontSize) }
            },
            valueRange = 24f..48f,
            steps = 5,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                adapter.loadText(inputText)
                displayToken = adapter.currentToken()
                adapter.reset()
                displayToken = adapter.currentToken()
            }) {
                Text("Load")
            }
            Button(onClick = { isPlaying = !isPlaying }) {
                Text(if (isPlaying) "Pause" else "Start")
            }
            OutlinedButton(onClick = {
                adapter.rewind()
                displayToken = adapter.currentToken()
            }) {
                Text("Rewind")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                adapter.previousToken()?.let { displayToken = it }
            }) {
                Text("Prev")
            }
            OutlinedButton(onClick = {
                adapter.nextToken()?.let { displayToken = it }
            }) {
                Text("Next")
            }
            OutlinedButton(onClick = {
                adapter.reset()
                displayToken = adapter.currentToken()
                isPlaying = false
            }) {
                Text("Reset")
            }
        }
    }
}
