package com.heathen.hadeswatch.core.hud

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Regular octagon (flat top/bottom) for the Field HUD command orb.
 * Corner chamfer ratio for a regular octagon: 1 / (2 + sqrt(2)) ≈ 0.293.
 */
object OctagonShape : Shape {
    private const val Cut = 0.293f

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val w = size.width
        val h = size.height
        val cutX = w * Cut
        val cutY = h * Cut
        val path = Path().apply {
            moveTo(cutX, 0f)
            lineTo(w - cutX, 0f)
            lineTo(w, cutY)
            lineTo(w, h - cutY)
            lineTo(w - cutX, h)
            lineTo(cutX, h)
            lineTo(0f, h - cutY)
            lineTo(0f, cutY)
            close()
        }
        return Outline.Generic(path)
    }
}
