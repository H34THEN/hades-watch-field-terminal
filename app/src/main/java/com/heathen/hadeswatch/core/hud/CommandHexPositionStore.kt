package com.heathen.hadeswatch.core.hud

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import com.heathen.hadeswatch.core.settings.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CommandHexPositionStore(private val context: Context) {

    private object Keys {
        val X = floatPreferencesKey("field_hex_x")
        val Y = floatPreferencesKey("field_hex_y")
    }

    val position: Flow<HexPosition> = context.settingsDataStore.data.map { prefs ->
        HexPosition(
            xFraction = prefs[Keys.X] ?: HexPosition.Default.xFraction,
            yFraction = prefs[Keys.Y] ?: HexPosition.Default.yFraction,
        )
    }

    suspend fun save(position: HexPosition) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.X] = position.xFraction
            prefs[Keys.Y] = position.yFraction
        }
    }

    suspend fun reset() = save(HexPosition.Default)
}
