package com.heathen.hadeswatch.features.gateways

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Launches gateway URLs outside the trusted Hades Watch WebShell.
 * Gateway sessions do not share Hades Watch cookies.
 */
object GatewayLauncher {

    fun launch(context: Context, url: String) {
        val normalized = GatewayUrlValidator.normalize(url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(normalized)).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        context.startActivity(intent)
    }
}
