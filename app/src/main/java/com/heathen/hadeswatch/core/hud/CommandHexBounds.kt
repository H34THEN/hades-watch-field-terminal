package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

data class HexPosition(
    /** 0..1 horizontal fraction within the safe content area. */
    val xFraction: Float,
    /** 0..1 vertical fraction within the safe content area. */
    val yFraction: Float,
) {
    companion object {
        val Default = HexPosition(xFraction = 0.88f, yFraction = 0.82f)
    }
}

object CommandHexBounds {
    fun clamp(
        position: HexPosition,
        hexSize: Dp,
        safePadding: PaddingValues,
        containerWidthPx: Float,
        containerHeightPx: Float,
        density: Float,
    ): HexPosition {
        if (containerWidthPx <= 0f || containerHeightPx <= 0f) return position

        val hexPx = hexSize.value * density
        val left = safePadding.calculateLeftPadding(LayoutDirection.Ltr).value * density
        val top = safePadding.calculateTopPadding().value * density
        val right = safePadding.calculateRightPadding(LayoutDirection.Ltr).value * density
        val bottom = safePadding.calculateBottomPadding().value * density

        val minX = left + hexPx / 2f
        val maxX = containerWidthPx - right - hexPx / 2f
        val minY = top + hexPx / 2f
        val maxY = containerHeightPx - bottom - hexPx / 2f

        val safeW = (maxX - minX).coerceAtLeast(1f)
        val safeH = (maxY - minY).coerceAtLeast(1f)

        val xPx = minX + position.xFraction.coerceIn(0f, 1f) * safeW
        val yPx = minY + position.yFraction.coerceIn(0f, 1f) * safeH

        return HexPosition(
            xFraction = ((xPx - minX) / safeW).coerceIn(0f, 1f),
            yFraction = ((yPx - minY) / safeH).coerceIn(0f, 1f),
        )
    }

    fun hexSizeDp(size: FieldHexSize): Dp = when (size) {
        FieldHexSize.Small -> 52.dp
        FieldHexSize.Medium -> 64.dp
        FieldHexSize.Large -> 76.dp
    }

    fun opacityValue(level: FieldHexOpacity): Float = when (level) {
        FieldHexOpacity.Low -> 0.55f
        FieldHexOpacity.Medium -> 0.78f
        FieldHexOpacity.High -> 1f
    }
}

enum class FieldHexSize { Small, Medium, Large }

enum class FieldHexOpacity { Low, Medium, High }
