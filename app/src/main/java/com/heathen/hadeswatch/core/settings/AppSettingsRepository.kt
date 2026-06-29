package com.heathen.hadeswatch.core.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingsRepository(private val context: Context) {

    private object Keys {
        val REDUCED_MOTION = booleanPreferencesKey(SettingsKeys.REDUCED_MOTION)
        val HIGH_CONTRAST = booleanPreferencesKey(SettingsKeys.HIGH_CONTRAST)
        val LARGE_TEXT = booleanPreferencesKey(SettingsKeys.LARGE_TEXT)
        val OPEN_EXTERNAL_IN_BROWSER = booleanPreferencesKey(SettingsKeys.OPEN_EXTERNAL_IN_BROWSER)
        val TOOL_K0READER = booleanPreferencesKey(SettingsKeys.TOOL_K0READER_ENABLED)
        val TOOL_ARES = booleanPreferencesKey(SettingsKeys.TOOL_ARES_ENABLED)
        val TOOL_FIELD_NOTES = booleanPreferencesKey(SettingsKeys.TOOL_FIELD_NOTES_ENABLED)
        val TOOL_GATEWAYS = booleanPreferencesKey(SettingsKeys.TOOL_GATEWAYS_ENABLED)
        val TOOL_SIGNAL_READER = booleanPreferencesKey(SettingsKeys.TOOL_SIGNAL_READER_ENABLED)
        val K0READER_USE_SDK = booleanPreferencesKey(SettingsKeys.K0READER_USE_SDK)
        val FIELD_NOTES_DRAFT = stringPreferencesKey(SettingsKeys.FIELD_NOTES_DRAFT)
        val GATEWAYS_DATA = stringPreferencesKey(SettingsKeys.GATEWAYS_DATA)
        val SIGNAL_SNIPPETS_DATA = stringPreferencesKey(SettingsKeys.SIGNAL_SNIPPETS_DATA)
        val K0READER_WPM = intPreferencesKey(SettingsKeys.K0READER_WPM)
        val K0READER_CHUNK = intPreferencesKey(SettingsKeys.K0READER_CHUNK_SIZE)
        val K0READER_FONT = intPreferencesKey(SettingsKeys.K0READER_FONT_SIZE)
    }

    val reducedMotion: Flow<Boolean> = context.settingsDataStore.data.map { it[Keys.REDUCED_MOTION] ?: false }
    val highContrast: Flow<Boolean> = context.settingsDataStore.data.map { it[Keys.HIGH_CONTRAST] ?: false }
    val largeText: Flow<Boolean> = context.settingsDataStore.data.map { it[Keys.LARGE_TEXT] ?: false }
    val openExternalInBrowser: Flow<Boolean> =
        context.settingsDataStore.data.map { it[Keys.OPEN_EXTERNAL_IN_BROWSER] ?: true }
    val k0ReaderEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[Keys.TOOL_K0READER] ?: true }
    val aresEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[Keys.TOOL_ARES] ?: true }
    val fieldNotesEnabled: Flow<Boolean> =
        context.settingsDataStore.data.map { it[Keys.TOOL_FIELD_NOTES] ?: true }
    val gatewaysEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[Keys.TOOL_GATEWAYS] ?: true }
    val signalReaderEnabled: Flow<Boolean> =
        context.settingsDataStore.data.map { it[Keys.TOOL_SIGNAL_READER] ?: true }
    val k0ReaderUseSdkAdapter: Flow<Boolean> =
        context.settingsDataStore.data.map { it[Keys.K0READER_USE_SDK] ?: true }
    val fieldNotesDraft: Flow<String> = context.settingsDataStore.data.map { it[Keys.FIELD_NOTES_DRAFT] ?: "" }
    val k0ReaderWpm: Flow<Int> = context.settingsDataStore.data.map { it[Keys.K0READER_WPM] ?: 300 }
    val k0ReaderChunkSize: Flow<Int> = context.settingsDataStore.data.map { it[Keys.K0READER_CHUNK] ?: 1 }
    val k0ReaderFontSize: Flow<Int> = context.settingsDataStore.data.map { it[Keys.K0READER_FONT] ?: 32 }

    suspend fun setReducedMotion(value: Boolean) = setBoolean(Keys.REDUCED_MOTION, value)
    suspend fun setHighContrast(value: Boolean) = setBoolean(Keys.HIGH_CONTRAST, value)
    suspend fun setLargeText(value: Boolean) = setBoolean(Keys.LARGE_TEXT, value)
    suspend fun setOpenExternalInBrowser(value: Boolean) = setBoolean(Keys.OPEN_EXTERNAL_IN_BROWSER, value)
    suspend fun setK0ReaderEnabled(value: Boolean) = setBoolean(Keys.TOOL_K0READER, value)
    suspend fun setAresEnabled(value: Boolean) = setBoolean(Keys.TOOL_ARES, value)
    suspend fun setFieldNotesEnabled(value: Boolean) = setBoolean(Keys.TOOL_FIELD_NOTES, value)
    suspend fun setGatewaysEnabled(value: Boolean) = setBoolean(Keys.TOOL_GATEWAYS, value)
    suspend fun setSignalReaderEnabled(value: Boolean) = setBoolean(Keys.TOOL_SIGNAL_READER, value)
    suspend fun setK0ReaderUseSdkAdapter(value: Boolean) = setBoolean(Keys.K0READER_USE_SDK, value)
    suspend fun setFieldNotesDraft(value: String) = setString(Keys.FIELD_NOTES_DRAFT, value)
    suspend fun setK0ReaderWpm(value: Int) = setInt(Keys.K0READER_WPM, value)
    suspend fun setK0ReaderChunkSize(value: Int) = setInt(Keys.K0READER_CHUNK, value)
    suspend fun setK0ReaderFontSize(value: Int) = setInt(Keys.K0READER_FONT, value)

    suspend fun clearLocalToolData() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(Keys.FIELD_NOTES_DRAFT)
            prefs.remove(Keys.GATEWAYS_DATA)
            prefs.remove(Keys.SIGNAL_SNIPPETS_DATA)
            prefs.remove(Keys.K0READER_WPM)
            prefs.remove(Keys.K0READER_CHUNK)
            prefs.remove(Keys.K0READER_FONT)
        }
    }

    suspend fun clearGatewayData() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(Keys.GATEWAYS_DATA)
        }
    }

    suspend fun clearSignalReaderData() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(Keys.SIGNAL_SNIPPETS_DATA)
        }
    }

    suspend fun clearK0ReaderPreferences() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(Keys.K0READER_WPM)
            prefs.remove(Keys.K0READER_CHUNK)
            prefs.remove(Keys.K0READER_FONT)
        }
    }

    private suspend fun setBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.settingsDataStore.edit { it[key] = value }
    }

    private suspend fun setString(key: Preferences.Key<String>, value: String) {
        context.settingsDataStore.edit { it[key] = value }
    }

    private suspend fun setInt(key: Preferences.Key<Int>, value: Int) {
        context.settingsDataStore.edit { it[key] = value }
    }
}
