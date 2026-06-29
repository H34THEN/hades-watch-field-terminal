package com.heathen.hadeswatch

import android.app.Application
import com.heathen.hadeswatch.core.security.SessionManager
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.features.gateways.GatewayRepository

class HadesWatchApp : Application() {
    lateinit var settingsRepository: AppSettingsRepository
        private set
    lateinit var sessionManager: SessionManager
        private set
    lateinit var gatewayRepository: GatewayRepository
        private set

    override fun onCreate() {
        super.onCreate()
        settingsRepository = AppSettingsRepository(this)
        sessionManager = SessionManager(this)
        gatewayRepository = GatewayRepository(this)
    }
}
