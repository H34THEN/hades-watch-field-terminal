package com.heathen.hadeswatch.core.hud

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CommandHexState {
    var isMenuOpen by mutableStateOf(false)
    var isDragging by mutableStateOf(false)
    var hiddenUntilMs by mutableLongStateOf(0L)

    fun isHidden(nowMs: Long = System.currentTimeMillis()): Boolean = nowMs < hiddenUntilMs

    fun hideTemporarily(seconds: Int = 10) {
        hiddenUntilMs = System.currentTimeMillis() + seconds * 1000L
        isMenuOpen = false
    }
}
