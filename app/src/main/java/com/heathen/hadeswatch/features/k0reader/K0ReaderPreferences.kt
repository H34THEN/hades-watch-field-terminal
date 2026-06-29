package com.heathen.hadeswatch.features.k0reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import kotlinx.coroutines.flow.Flow

class K0ReaderPreferences(private val repository: AppSettingsRepository) {
    val wpm: Flow<Int> = repository.k0ReaderWpm
    val chunkSize: Flow<Int> = repository.k0ReaderChunkSize
    val fontSize: Flow<Int> = repository.k0ReaderFontSize

    suspend fun saveWpm(value: Int) = repository.setK0ReaderWpm(value)
    suspend fun saveChunkSize(value: Int) = repository.setK0ReaderChunkSize(value)
    suspend fun saveFontSize(value: Int) = repository.setK0ReaderFontSize(value)
}

@Composable
fun rememberK0ReaderAdapter(): LocalK0ReaderAdapter = remember { LocalK0ReaderAdapter() }
