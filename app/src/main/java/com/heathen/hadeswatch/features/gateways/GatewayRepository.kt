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
        GatewayStorage.decode(prefs[gatewaysKey])
    }

    suspend fun saveAll(gateways: List<GatewayDefinition>) {
        context.settingsDataStore.edit { prefs ->
            prefs[gatewaysKey] = GatewayStorage.encode(gateways)
        }
    }

    suspend fun upsert(gateway: GatewayDefinition) {
        context.settingsDataStore.edit { prefs ->
            val current = GatewayStorage.decode(prefs[gatewaysKey]).toMutableList()
            val index = current.indexOfFirst { it.id == gateway.id }
            if (index >= 0) {
                current[index] = gateway
            } else {
                current.add(gateway)
            }
            prefs[gatewaysKey] = GatewayStorage.encode(current)
        }
    }

    suspend fun delete(id: String) {
        context.settingsDataStore.edit { prefs ->
            val current = GatewayStorage.decode(prefs[gatewaysKey]).filterNot { it.id == id }
            prefs[gatewaysKey] = GatewayStorage.encode(current)
        }
    }

    suspend fun clearAll() {
        context.settingsDataStore.edit { prefs ->
            prefs.remove(gatewaysKey)
        }
    }
}
