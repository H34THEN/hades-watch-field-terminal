package com.heathen.hadeswatch.core.hud

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
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

data class HexDimensions(
    val width: Dp,
    val height: Dp,
)

object CommandHexBounds {
    fun clamp(
        position: HexPosition,
        dimensions: HexDimensions,
        safePadding: PaddingValues,
        containerWidthPx: Float,
        containerHeightPx: Float,
        density: Float,
    ): HexPosition {
        if (containerWidthPx <= 0f || containerHeightPx <= 0f) return position

        val halfW = dimensions.width.value * density / 2f
        val halfH = dimensions.height.value * density / 2f
        val left = safePadding.calculateLeftPadding(LayoutDirection.Ltr).value * density
        val top = safePadding.calculateTopPadding().value * density
        val right = safePadding.calculateRightPadding(LayoutDirection.Ltr).value * density
        val bottom = safePadding.calculateBottomPadding().value * density

        val minX = left + halfW
        val maxX = containerWidthPx - right - halfW
        val minY = top + halfH
        val maxY = containerHeightPx - bottom - halfH

        val safeW = (maxX - minX).coerceAtLeast(1f)
        val safeH = (maxY - minY).coerceAtLeast(1f)

        val center = centerFromFraction(
            position = position,
            safeLeftPx = left,
            safeTopPx = top,
            safeWidthPx = containerWidthPx - left - right,
            safeHeightPx = containerHeightPx - top - bottom,
            halfWidthPx = halfW,
            halfHeightPx = halfH,
        )

        return fractionFromCenter(
            centerX = center.x,
            centerY = center.y,
            safeLeftPx = left,
            safeTopPx = top,
            safeWidthPx = containerWidthPx - left - right,
            safeHeightPx = containerHeightPx - top - bottom,
            halfWidthPx = halfW,
            halfHeightPx = halfH,
        )
    }

    fun centerFromFraction(
        position: HexPosition,
        safeLeftPx: Float,
        safeTopPx: Float,
        safeWidthPx: Float,
        safeHeightPx: Float,
        halfWidthPx: Float,
        halfHeightPx: Float,
    ): Offset {
        val minX = safeLeftPx + halfWidthPx
        val minY = safeTopPx + halfHeightPx
        val safeW = (safeWidthPx - halfWidthPx * 2f).coerceAtLeast(1f)
        val safeH = (safeHeightPx - halfHeightPx * 2f).coerceAtLeast(1f)
        return Offset(
            x = minX + position.xFraction.coerceIn(0f, 1f) * safeW,
            y = minY + position.yFraction.coerceIn(0f, 1f) * safeH,
        )
    }

    fun fractionFromCenter(
        centerX: Float,
        centerY: Float,
        safeLeftPx: Float,
        safeTopPx: Float,
        safeWidthPx: Float,
        safeHeightPx: Float,
        halfWidthPx: Float,
        halfHeightPx: Float,
    ): HexPosition {
        val minX = safeLeftPx + halfWidthPx
        val minY = safeTopPx + halfHeightPx
        val safeW = (safeWidthPx - halfWidthPx * 2f).coerceAtLeast(1f)
        val safeH = (safeHeightPx - halfHeightPx * 2f).coerceAtLeast(1f)
        val xPx = centerX.coerceIn(minX, minX + safeW)
        val yPx = centerY.coerceIn(minY, minY + safeH)
        return HexPosition(
            xFraction = ((xPx - minX) / safeW).coerceIn(0f, 1f),
            yFraction = ((yPx - minY) / safeH).coerceIn(0f, 1f),
        )
    }

    fun hexDimensions(size: FieldHexSize): HexDimensions {
        val base = hexSizeDp(size)
        return HexDimensions(
            width = base,
            height = base * 0.82f,
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

    fun halfExtentsPx(dimensions: HexDimensions, density: Density): Pair<Float, Float> {
        val halfW = with(density) { dimensions.width.toPx() / 2f }
        val halfH = with(density) { dimensions.height.toPx() / 2f }
        return halfW to halfH
    }
}

enum class FieldHexSize { Small, Medium, Large }

enum class FieldHexOpacity { Low, Medium, High }
