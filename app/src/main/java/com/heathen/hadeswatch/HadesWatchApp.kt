package com.heathen.hadeswatch

import android.app.Application
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.core.security.SessionManager

class HadesWatchApp : Application() {
    lateinit var settingsRepository: AppSettingsRepository
        private set
    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        settingsRepository = AppSettingsRepository(this)
        sessionManager = SessionManager(this)
    }
}
