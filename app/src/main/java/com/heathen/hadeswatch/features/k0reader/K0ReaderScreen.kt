package com.heathen.hadeswatch.features.k0reader

import androidx.compose.animation.AnimatedVisibility
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import com.heathen.hadeswatch.core.ui.HadesEmptyState
import com.heathen.hadeswatch.core.ui.HadesPrimaryButton
import com.heathen.hadeswatch.core.ui.HadesSecondaryButton
import com.heathen.hadeswatch.core.ui.HadesSectionHeader
import com.heathen.hadeswatch.core.ui.HadesTerminalCard
import com.heathen.hadeswatch.core.ui.HadesWarningBox
import com.heathen.hadeswatch.features.k0reader.epub.EpubDocumentLoader
import com.heathen.k0r34d3r.core.epub.EpubImportException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

/** Keep paste field small — full EPUB text must never be bound to [OutlinedTextField]. */
private const val MAX_PASTE_FIELD_CHARS = 20_000

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun K0ReaderScreen(
    settingsRepository: AppSettingsRepository,
    reducedMotion: Boolean = false,
) {
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val context = LocalContext.current
    val prefs = remember { K0ReaderPreferences(settingsRepository) }
    val useSdk by settingsRepository.k0ReaderUseSdkAdapter.collectAsState(initial = true)
    val adapter = rememberDefaultK0ReaderAdapter(useSdk = useSdk)

    var inputText by remember { mutableStateOf("") }
    var displayToken by remember { mutableStateOf("") }
    var isPlaying by remember { mutableStateOf(false) }
    var isLoaded by remember { mutableStateOf(false) }
    var wpm by remember { mutableIntStateOf(300) }
    var chunkSize by remember { mutableIntStateOf(1) }
    var fontSize by remember { mutableIntStateOf(32) }
    var progress by remember { mutableStateOf(0f) }
    var transferSource by remember { mutableStateOf<String?>(null) }
    var focusMode by remember { mutableStateOf(false) }
    var punctuationPause by remember { mutableStateOf(true) }
    var orpEnabled by remember { mutableStateOf(true) }
    var saveSession by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(true) }
    var epubTitle by remember { mutableStateOf<String?>(null) }
    var isParsingEpub by remember { mutableStateOf(false) }
    var epubError by remember { mutableStateOf<String?>(null) }

    fun syncDisplayFromAdapter() {
        displayToken = adapter.currentToken()
        progress = adapter.progressPercent()
        isLoaded = adapter.hasText()
    }

    fun loadTextIntoReader(text: String = inputText) {
        adapter.loadText(text)
        adapter.setWordsPerMinute(wpm)
        adapter.setChunkSize(chunkSize)
        adapter.reset()
        syncDisplayFromAdapter()
        isPlaying = false
        if (saveSession && text.isNotBlank()) {
            scope.launch { prefs.saveLastText(text) }
        }
    }

    fun loadImportedBook(title: String, plainText: String) {
        epubTitle = title
        transferSource = null
        inputText = ""
        loadTextIntoReader(plainText)
    }

    val epubPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            isParsingEpub = true
            epubError = null
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            } catch (_: SecurityException) {
                // ephemeral read is fine for one session
            }
            try {
                val result = withContext(Dispatchers.IO) {
                    EpubDocumentLoader.loadFromUri(context, uri)
                }
                loadImportedBook(result.title, result.plainText)
            } catch (e: EpubImportException) {
                epubError = e.message
            } catch (e: OutOfMemoryError) {
                epubError = "This EPUB is too large for available memory."
            } catch (_: Exception) {
                epubError = "Unable to import EPUB."
            } finally {
                isParsingEpub = false
            }
        }
    }

    LaunchedEffect(Unit) {
        wpm = prefs.wpm.first()
        chunkSize = prefs.chunkSize.first()
        fontSize = prefs.fontSize.first()
        focusMode = prefs.focusMode.first()
        punctuationPause = prefs.punctuationPause.first()
        orpEnabled = prefs.orpEnabled.first()
        saveSession = prefs.saveSession.first()
        ReaderTransferRepository.consumePending()?.let { pending ->
            if (pending.text.length > MAX_PASTE_FIELD_CHARS) {
                loadTextIntoReader(pending.text)
            } else {
                inputText = pending.text
            }
            transferSource = pending.sourceTitle.takeIf { it.isNotBlank() }
        } ?: run {
            if (prefs.saveSession.first()) {
                val last = prefs.lastText.first()
                if (last.isNotBlank()) {
                    if (last.length > MAX_PASTE_FIELD_CHARS) {
                        loadTextIntoReader(last)
                    } else {
                        inputText = last
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            view.keepScreenOn = false
        }
    }

    LaunchedEffect(isPlaying) {
        view.keepScreenOn = isPlaying
    }

    LaunchedEffect(isPlaying, wpm, reducedMotion, punctuationPause) {
        if (!isPlaying || reducedMotion) return@LaunchedEffect
        while (isActive && isPlaying) {
            val pauseMs = computePauseMillis(adapter.intervalMillis(), displayToken, punctuationPause)
            delay(pauseMs)
            val next = adapter.nextToken()
            if (next == null) {
                isPlaying = false
            } else {
                displayToken = next
                progress = adapter.progressPercent()
            }
        }
    }

    val currentIndex = adapter.currentIndex()
    val prevToken = if (currentIndex > 0) adapter.tokenAt(currentIndex - 1).orEmpty() else ""
    val nextPreview = adapter.tokenAt(currentIndex + 1).orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HadesSectionHeader(
            title = "k0R34DER",
            subtitle = "Local RSVP speed reader — no upload",
        )
        Text(
            text = if (useSdk) "Engine: K0R34D3R Kotlin core" else "Engine: local fallback",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
        )

        transferSource?.let { source ->
            HadesTerminalCard(title = "Local transfer") {
                Text("Loaded from Signal Reader: $source", color = SignalCyan)
                Text("In-memory only — not uploaded.", color = MutedText, modifier = Modifier.padding(top = 4.dp))
                HadesSecondaryButton(
                    text = "Clear transfer",
                    onClick = {
                        inputText = ""
                        transferSource = null
                        epubTitle = null
                        ReaderTransferRepository.clearPending()
                        adapter.loadText("")
                        displayToken = ""
                        progress = 0f
                        isPlaying = false
                        isLoaded = false
                    },
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }

        epubTitle?.let { title ->
            HadesTerminalCard(title = "Imported EPUB") {
                Text(title, color = SignalCyan)
                Text(
                    "${adapter.tokenCount()} tokens — local only, not uploaded.",
                    color = MutedText,
                    modifier = Modifier.padding(top = 4.dp),
                )
                HadesSecondaryButton(
                    text = "Clear imported book",
                    onClick = {
                        inputText = ""
                        epubTitle = null
                        adapter.loadText("")
                        displayToken = ""
                        progress = 0f
                        isPlaying = false
                        isLoaded = false
                    },
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }

        if (isParsingEpub) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 12.dp))
                Text("Parsing EPUB…", color = MutedText)
            }
        }

        epubError?.let { message ->
            HadesWarningBox(message = message)
        }

        AnimatedVisibility(visible = !focusMode || !isPlaying) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                HadesSecondaryButton(
                    text = "Import EPUB",
                    onClick = {
                        epubPicker.launch(
                            arrayOf(
                                "application/epub+zip",
                                "application/x-epub+zip",
                                "application/octet-stream",
                                "application/zip",
                            ),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isParsingEpub,
                )
                HadesSecondaryButton(
                    text = if (showSettings) "Hide settings" else "Settings",
                    onClick = { showSettings = !showSettings },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        HadesTerminalCard(title = "Reader") {
            if (prevToken.isNotBlank() || nextPreview.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = prevToken,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = nextPreview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            K0OrpTokenDisplay(
                token = displayToken,
                fontSize = fontSize.sp,
                orpEnabled = orpEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = if (adapter.tokenCount() > 0) {
                    "${currentIndex + 1} / ${adapter.tokenCount()} (${(progress * 100).toInt()}%)"
                } else {
                    "No text loaded"
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HadesPrimaryButton(
                text = if (isPlaying) "Pause" else "Start",
                onClick = {
                    if (!isLoaded && inputText.isNotBlank()) loadTextIntoReader()
                    if (adapter.hasText()) {
                        isPlaying = !isPlaying
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = adapter.hasText() || inputText.isNotBlank(),
            )
            HadesSecondaryButton(
                text = "Rewind",
                onClick = {
                    adapter.reset()
                    syncDisplayFromAdapter()
                    isPlaying = false
                },
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HadesSecondaryButton(
                text = "Previous",
                onClick = {
                    adapter.previousToken()?.let {
                        displayToken = it
                        progress = adapter.progressPercent()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = isLoaded,
            )
            HadesSecondaryButton(
                text = "Next",
                onClick = {
                    adapter.nextToken()?.let {
                        displayToken = it
                        progress = adapter.progressPercent()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = isLoaded,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HadesSecondaryButton(
                text = "Slower",
                onClick = {
                    wpm = (wpm - 25).coerceAtLeast(100)
                    adapter.setWordsPerMinute(wpm)
                    scope.launch { prefs.saveWpm(wpm) }
                },
                modifier = Modifier.weight(1f),
            )
            Text("$wpm WPM", style = MaterialTheme.typography.titleMedium, color = TerminalGreen)
            HadesSecondaryButton(
                text = "Faster",
                onClick = {
                    wpm = (wpm + 25).coerceAtMost(800)
                    adapter.setWordsPerMinute(wpm)
                    scope.launch { prefs.saveWpm(wpm) }
                },
                modifier = Modifier.weight(1f),
            )
        }

        AnimatedVisibility(visible = !focusMode || !isPlaying) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { draft ->
                        if (draft.length <= MAX_PASTE_FIELD_CHARS) {
                            inputText = draft
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (epubTitle != null) {
                                "Paste new text to replace EPUB"
                            } else {
                                "Paste or edit text"
                            },
                        )
                    },
                    placeholder = {
                        if (epubTitle != null) {
                            Text("Book loaded from EPUB — paste here to replace")
                        }
                    },
                    minLines = 3,
                    maxLines = 8,
                    enabled = !isParsingEpub,
                )
                if (inputText.length >= MAX_PASTE_FIELD_CHARS) {
                    Text(
                        "Paste limit reached (${MAX_PASTE_FIELD_CHARS} chars). Use Import EPUB for full books.",
                        color = MutedText,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HadesPrimaryButton(text = "Load text", onClick = { loadTextIntoReader() })
                    HadesSecondaryButton(text = "Clear", onClick = {
                        inputText = ""
                        epubTitle = null
                        adapter.loadText("")
                        displayToken = ""
                        progress = 0f
                        isLoaded = false
                        isPlaying = false
                        scope.launch { prefs.clearSession() }
                    })
                }
            }
        }

        AnimatedVisibility(visible = showSettings && (!focusMode || !isPlaying)) {
            HadesTerminalCard(title = "Settings") {
                Text("Words per minute: $wpm")
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
                Text("Chunk mode", modifier = Modifier.padding(top = 8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1 to "1 word", 2 to "2 words", 3 to "3 words", 4 to "Phrase").forEach { (size, label) ->
                        FilterChip(
                            selected = chunkSize == size,
                            onClick = {
                                chunkSize = size
                                adapter.setChunkSize(size)
                                scope.launch { prefs.saveChunkSize(size) }
                                if (isLoaded) syncDisplayFromAdapter()
                            },
                            label = { Text(label) },
                        )
                    }
                }
                Text("Font size: $fontSize", modifier = Modifier.padding(top = 8.dp))
                Slider(
                    value = fontSize.toFloat(),
                    onValueChange = {
                        fontSize = it.toInt()
                        scope.launch { prefs.saveFontSize(fontSize) }
                    },
                    valueRange = 24f..56f,
                    steps = 7,
                )
                SettingToggle("Focus mode (hide input while playing)", focusMode) {
                    focusMode = it
                    scope.launch { prefs.saveFocusMode(it) }
                }
                SettingToggle("Punctuation pause", punctuationPause) {
                    punctuationPause = it
                    scope.launch { prefs.savePunctuationPause(it) }
                }
                SettingToggle("ORP focus highlight", orpEnabled) {
                    orpEnabled = it
                    scope.launch { prefs.saveOrpEnabled(it) }
                }
                SettingToggle("Save last text locally", saveSession) {
                    saveSession = it
                    scope.launch { prefs.saveSaveSession(it) }
                }
            }
        }

        if (inputText.isBlank() && !isLoaded && epubTitle == null) {
            HadesEmptyState(
                title = "No reading text",
                message = "Paste lore, import an EPUB, or open a snippet from Signal Reader.",
            )
        }
    }
}

@Composable
private fun SettingToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
