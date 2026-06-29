package com.heathen.hadeswatch.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

internal val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "hades_settings",
)
