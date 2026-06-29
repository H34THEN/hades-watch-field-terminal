package com.heathen.hadeswatch.core.navigation

import com.heathen.hadeswatch.core.hud.resolveTabRoute

fun routeForBottomNav(route: String?): String =
    resolveTabRoute(route ?: HadesDestination.WebHub.tabRoute)
