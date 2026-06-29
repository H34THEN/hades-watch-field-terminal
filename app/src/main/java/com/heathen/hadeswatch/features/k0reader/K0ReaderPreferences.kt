package com.heathen.hadeswatch.features.k0reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import kotlinx.coroutines.flow.Flow

class K0ReaderPreferences(private val repository: AppSettingsRepository) {
    val wpm: Flow<Int> = repository.k0ReaderWpm
    val chunkSize: Flow<Int> = repository.k0ReaderChunkSize
    val fontSize: Flow<Int> = repository.k0ReaderFontSize
    val useSdkAdapter: Flow<Boolean> = repository.k0ReaderUseSdkAdapter
    val focusMode: Flow<Boolean> = repository.k0ReaderFocusMode
    val punctuationPause: Flow<Boolean> = repository.k0ReaderPunctuationPause
    val orpEnabled: Flow<Boolean> = repository.k0ReaderOrpEnabled
    val saveSession: Flow<Boolean> = repository.k0ReaderSaveSession
    val lastText: Flow<String> = repository.k0ReaderLastText

    suspend fun saveWpm(value: Int) = repository.setK0ReaderWpm(value)
    suspend fun saveChunkSize(value: Int) = repository.setK0ReaderChunkSize(value)
    suspend fun saveFontSize(value: Int) = repository.setK0ReaderFontSize(value)
    suspend fun saveUseSdkAdapter(value: Boolean) = repository.setK0ReaderUseSdkAdapter(value)
    suspend fun saveFocusMode(value: Boolean) = repository.setK0ReaderFocusMode(value)
    suspend fun savePunctuationPause(value: Boolean) = repository.setK0ReaderPunctuationPause(value)
    suspend fun saveOrpEnabled(value: Boolean) = repository.setK0ReaderOrpEnabled(value)
    suspend fun saveSaveSession(value: Boolean) = repository.setK0ReaderSaveSession(value)
    suspend fun saveLastText(value: String) = repository.setK0ReaderLastText(value)
    suspend fun clearSession() = repository.clearK0ReaderSession()
}

@Composable
fun rememberDefaultK0ReaderAdapter(useSdk: Boolean = true): K0ReaderAdapter =
    remember(useSdk) {
        if (useSdk) K0SdkReaderAdapter() else LocalK0ReaderAdapter()
    }

@Composable
fun rememberLocalK0ReaderAdapter(): LocalK0ReaderAdapter = remember { LocalK0ReaderAdapter() }

fun computePauseMillis(baseInterval: Long, token: String, punctuationPause: Boolean): Long {
    if (!punctuationPause || token.isBlank()) return baseInterval
    return when {
        token.matches(Regex(".*[.!?]$")) -> (baseInterval * 1.6).toLong()
        token.matches(Regex(".*[;:]$")) -> (baseInterval * 1.35).toLong()
        token.matches(Regex(".*[,]$")) -> (baseInterval * 1.2).toLong()
        else -> baseInterval
    }
}
