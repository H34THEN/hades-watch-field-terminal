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
        SignalSnippetStorage.decode(prefs[key]).sortedWith(
            compareByDescending<SignalSnippet> { it.updatedAt }.thenBy { it.sortOrder },
        )
    }

    suspend fun upsert(snippet: SignalSnippet) {
        context.settingsDataStore.edit { prefs ->
            val current = SignalSnippetStorage.decode(prefs[key]).toMutableList()
            val now = System.currentTimeMillis()
            val toSave = snippet.copy(
                tags = SignalSnippetStorage.sanitizeTags(snippet.tags),
                updatedAt = now,
            )
            val index = current.indexOfFirst { it.id == snippet.id }
            if (index >= 0) {
                current[index] = toSave
            } else {
                current.add(toSave.copy(createdAt = now))
            }
            prefs[key] = SignalSnippetStorage.encodeArrayOnly(current)
        }
    }

    suspend fun delete(id: String) {
        context.settingsDataStore.edit { prefs ->
            val current = SignalSnippetStorage.decode(prefs[key]).filterNot { it.id == id }
            prefs[key] = SignalSnippetStorage.encodeArrayOnly(current)
        }
    }

    suspend fun clearAll() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

    fun exportJson(snippets: List<SignalSnippet>): String = SignalSnippetStorage.encode(snippets)

    suspend fun mergeImport(
        incoming: List<SignalSnippet>,
        existingIds: Set<String>,
        overwrite: Boolean = false,
        invalidCount: Int = 0,
    ): SignalSnippetStorage.ImportResult {
        var added = 0
        var skipped = 0
        var duplicate = 0
        context.settingsDataStore.edit { prefs ->
            val current = SignalSnippetStorage.decode(prefs[key]).toMutableList()
            val ids = current.map { it.id }.toMutableSet()
            val titleSourceKeys = current.map { "${it.title.lowercase()}|${it.sourceLabel.lowercase()}" }.toMutableSet()

            incoming.forEach { snippet ->
                val titleKey = "${snippet.title.lowercase()}|${snippet.sourceLabel.lowercase()}"
                if (snippet.id in ids && !overwrite) {
                    duplicate++
                    skipped++
                    return@forEach
                }
                if (titleKey in titleSourceKeys && snippet.id !in ids) {
                    duplicate++
                }
                val index = current.indexOfFirst { it.id == snippet.id }
                if (index >= 0 && overwrite) {
                    current[index] = snippet
                } else if (index < 0) {
                    current.add(snippet)
                    ids.add(snippet.id)
                    titleSourceKeys.add(titleKey)
                    added++
                } else {
                    skipped++
                }
            }
            prefs[key] = SignalSnippetStorage.encodeArrayOnly(current)
        }
        return SignalSnippetStorage.ImportResult(
            added = added,
            skipped = skipped,
            duplicate = duplicate,
            invalid = invalidCount,
        )
    }
}
