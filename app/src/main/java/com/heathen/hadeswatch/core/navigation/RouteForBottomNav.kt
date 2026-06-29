package com.heathen.hadeswatch.core.navigation

fun routeForBottomNav(route: String?): String = when {
    route?.startsWith("web") == true -> HadesDestination.WebHub.tabRoute
    route == HadesDestination.K0Reader.route -> HadesDestination.Reader.tabRoute
    route == HadesDestination.Ares.route -> HadesDestination.Tools.tabRoute
    route == HadesDestination.FieldNotes.route -> HadesDestination.Tools.tabRoute
    route == HadesDestination.UnderworldGateways.route -> HadesDestination.Tools.tabRoute
    route == HadesDestination.SignalReader.route -> HadesDestination.Tools.tabRoute
    route?.startsWith("tools/") == true -> HadesDestination.Tools.tabRoute
    route == HadesDestination.PrivacySafety.route -> HadesDestination.Settings.tabRoute
    route == HadesDestination.LocalToolData.route -> HadesDestination.Settings.tabRoute
    route == HadesDestination.FutureApiStatus.route -> HadesDestination.Settings.tabRoute
    else -> route?.substringBefore("?") ?: HadesDestination.Home.tabRoute
}
