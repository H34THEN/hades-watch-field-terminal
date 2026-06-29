package com.heathen.hadeswatch.features.notifications

import androidx.compose.runtime.Composable
import com.heathen.hadeswatch.core.navigation.WebRoutes
import com.heathen.hadeswatch.features.webshell.WebShellScreen

@Composable
fun NotificationsScreen(openExternalInBrowser: Boolean = true) {
    WebShellScreen(url = WebRoutes.NOTIFICATIONS, openExternalInBrowser = openExternalInBrowser)
}
