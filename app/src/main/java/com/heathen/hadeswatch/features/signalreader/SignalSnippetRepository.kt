package com.heathen.hadeswatch.features.signalreader

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.heathen.hadeswatch.core.settings.SettingsKeys
import com.heathen.hadeswatch.core.settings.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SignalSnippetRepository(private val context: Context) {

    private val key = stringPreferencesKey(SettingsKeys.SIGNAL_SNIPPETS_DATA)

    val snippets: Flow<List<SignalSnippet>> = context.settingsDataStore.data.map { prefs ->
        SignalSnippetStorage.decode(prefs[key]).sortedByDescending { it.updatedAt }
    }

    suspend fun upsert(snippet: SignalSnippet) {
        context.settingsDataStore.edit { prefs ->
            val current = SignalSnippetStorage.decode(prefs[key]).toMutableList()
            val now = System.currentTimeMillis()
            val toSave = snippet.copy(updatedAt = now)
            val index = current.indexOfFirst { it.id == snippet.id }
            if (index >= 0) {
                current[index] = toSave
            } else {
                current.add(toSave.copy(createdAt = now))
            }
            prefs[key] = SignalSnippetStorage.encode(current)
        }
    }

    suspend fun delete(id: String) {
        context.settingsDataStore.edit { prefs ->
            val current = SignalSnippetStorage.decode(prefs[key]).filterNot { it.id == id }
            prefs[key] = SignalSnippetStorage.encode(current)
        }
    }

    suspend fun clearAll() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(key)
        }
    }
}
