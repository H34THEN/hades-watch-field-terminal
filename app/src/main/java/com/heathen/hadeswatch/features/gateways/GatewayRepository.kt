package com.heathen.hadeswatch.features.gateways

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.heathen.hadeswatch.core.settings.SettingsKeys
import com.heathen.hadeswatch.core.settings.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GatewayRepository(private val context: Context) {

    private val gatewaysKey = stringPreferencesKey(SettingsKeys.GATEWAYS_DATA)

    val gateways: Flow<List<GatewayDefinition>> = context.settingsDataStore.data.map { prefs ->
        GatewayStorage.decode(prefs[gatewaysKey]).sortedWith(
            compareBy<GatewayDefinition> { it.sortOrder }.thenBy { it.displayName.lowercase() },
        )
    }

    suspend fun saveAll(gateways: List<GatewayDefinition>) {
        context.settingsDataStore.edit { prefs ->
            prefs[gatewaysKey] = GatewayStorage.encodeArrayOnly(gateways)
        }
    }

    suspend fun upsert(gateway: GatewayDefinition) {
        context.settingsDataStore.edit { prefs ->
            val current = GatewayStorage.decode(prefs[gatewaysKey]).toMutableList()
            val now = System.currentTimeMillis()
            val toSave = gateway.copy(updatedAt = now)
            val index = current.indexOfFirst { it.id == gateway.id }
            if (index >= 0) {
                current[index] = toSave
            } else {
                current.add(toSave.copy(createdAt = now))
            }
            prefs[gatewaysKey] = GatewayStorage.encodeArrayOnly(current)
        }
    }

    suspend fun markOpened(id: String) {
        context.settingsDataStore.edit { prefs ->
            val current = GatewayStorage.decode(prefs[gatewaysKey]).map { g ->
                if (g.id == id) g.withOpenedNow() else g
            }
            prefs[gatewaysKey] = GatewayStorage.encodeArrayOnly(current)
        }
    }

    suspend fun delete(id: String) {
        context.settingsDataStore.edit { prefs ->
            val current = GatewayStorage.decode(prefs[gatewaysKey]).filterNot { it.id == id }
            prefs[gatewaysKey] = GatewayStorage.encodeArrayOnly(current)
        }
    }

    suspend fun clearAll() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(gatewaysKey)
        }
    }

    suspend fun mergeImport(incoming: List<GatewayDefinition>, overwrite: Boolean): Int {
        context.settingsDataStore.edit { prefs ->
            val current = GatewayStorage.decode(prefs[gatewaysKey]).toMutableList()
            val existingIds = current.map { it.id }.toSet()
            var added = 0
            incoming.forEach { gateway ->
                if (gateway.id in existingIds && !overwrite) return@forEach
                val index = current.indexOfFirst { it.id == gateway.id }
                if (index >= 0 && overwrite) {
                    current[index] = gateway
                } else if (index < 0) {
                    current.add(gateway)
                    added++
                }
            }
            prefs[gatewaysKey] = GatewayStorage.encodeArrayOnly(current)
        }
        return incoming.size
    }

    fun exportJson(gateways: List<GatewayDefinition>): String = GatewayStorage.encode(gateways)
}
