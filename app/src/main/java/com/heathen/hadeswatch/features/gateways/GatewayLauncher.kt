package com.heathen.hadeswatch.features.gateways

import android.content.Context
import android.content.Intent
import android.net.Uri

object GatewayLauncher {

    fun launchExternal(context: Context, url: String) {
        val normalized = GatewayUrlValidator.normalize(url)
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(normalized)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
            },
        )
    }

    fun launch(
        context: Context,
        gateway: GatewayDefinition,
        onOpenInAppViewer: (GatewayDefinition) -> Unit,
    ) {
        when (gateway.launchMode) {
            GatewayLaunchMode.EXTERNAL_BROWSER -> launchExternal(context, gateway.url)
            GatewayLaunchMode.ISOLATED_IN_APP_VIEWER -> onOpenInAppViewer(gateway)
        }
    }
}
