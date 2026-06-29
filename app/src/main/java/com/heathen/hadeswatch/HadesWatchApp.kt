package com.heathen.hadeswatch

import android.app.Application
import com.heathen.hadeswatch.core.security.SessionManager
import com.heathen.hadeswatch.core.settings.AppSettingsRepository
import com.heathen.hadeswatch.features.gateways.GatewayRepository
import com.heathen.hadeswatch.features.signalreader.SignalSnippetRepository

class HadesWatchApp : Application() {
    lateinit var settingsRepository: AppSettingsRepository
        private set
    lateinit var sessionManager: SessionManager
        private set
    lateinit var gatewayRepository: GatewayRepository
        private set
    lateinit var signalSnippetRepository: SignalSnippetRepository
        private set

    override fun onCreate() {
        super.onCreate()
        settingsRepository = AppSettingsRepository(this)
        sessionManager = SessionManager(this)
        gatewayRepository = GatewayRepository(this)
        signalSnippetRepository = SignalSnippetRepository(this)
    }
}
