package com.heathen.hadeswatch.core.api

enum class MobileApiAvailability {
    NOT_AVAILABLE,
    PLANNED,
    REQUIRES_CONSENT,
}

data class MobileApiEndpointStatus(
    val route: String,
    val method: String,
    val availability: MobileApiAvailability,
    val label: String,
)

object MobileApiStatus {
    val overallAvailability: MobileApiAvailability = MobileApiAvailability.NOT_AVAILABLE

    val endpointStatuses: List<MobileApiEndpointStatus> =
        MobileApiCapabilityRegistry.planned.map { info ->
            MobileApiEndpointStatus(
                route = info.route,
                method = info.method,
                availability = if (info.requiresUserConsent) {
                    MobileApiAvailability.REQUIRES_CONSENT
                } else {
                    MobileApiAvailability.PLANNED
                },
                label = info.summary,
            )
        }
}
