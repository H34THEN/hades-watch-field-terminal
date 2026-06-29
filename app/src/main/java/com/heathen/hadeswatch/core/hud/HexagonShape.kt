package com.heathen.hadeswatch.core.hud

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/** Flat-top hexagon for the Field Hex command orb. */
object HexagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.25f, 0f)
            lineTo(w * 0.75f, 0f)
            lineTo(w, h * 0.5f)
            lineTo(w * 0.75f, h)
            lineTo(w * 0.25f, h)
            lineTo(0f, h * 0.5f)
            close()
        }
        return Outline.Generic(path)
    }
}
