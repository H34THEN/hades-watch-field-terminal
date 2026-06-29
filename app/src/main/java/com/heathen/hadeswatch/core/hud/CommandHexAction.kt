package com.heathen.hadeswatch.core.hud

import com.heathen.hadeswatch.core.ui.ToolIconKey

enum class CommandHexActionGroup {
    Navigation,
    WebControls,
    Tools,
    Utility,
}

data class CommandHexAction(
    val id: String,
    val label: String,
    val group: CommandHexActionGroup,
    val iconKey: ToolIconKey? = null,
    val enabled: Boolean = true,
)
