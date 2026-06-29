package com.heathen.hadeswatch.core.permissions

object PermissionExplainer {
    const val MVP_PERMISSIONS = "INTERNET, ACCESS_NETWORK_STATE"

    val futureAresPermissions = listOf(
        "Bluetooth scan/connect (when 4R3S scanning is implemented)",
        "Nearby devices (when proximity features are implemented)",
        "Location (only where Android requires it for scanning)",
        "Wi-Fi state access (where applicable)",
    )

    fun aresFuturePermissionNote(): String =
        "4R3S permissions will only be requested when the feature is implemented and explicitly enabled by you."
}
