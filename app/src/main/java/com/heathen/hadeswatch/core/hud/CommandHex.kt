package com.heathen.hadeswatch.core.hud

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.heathen.hadeswatch.core.theme.PanelBorder
import com.heathen.hadeswatch.core.theme.PanelDark
import com.heathen.hadeswatch.core.theme.SignalCyan
import com.heathen.hadeswatch.core.theme.TerminalGreen
import kotlin.math.roundToInt

@Composable
fun CommandHex(
    position: HexPosition,
    dimensions: HexDimensions,
    opacity: Float,
    isMenuOpen: Boolean,
    onDragEnd: (HexPosition) -> Unit,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    onDragStateChange: (Boolean) -> Unit,
    safeLeftPx: Float,
    safeTopPx: Float,
    safeWidthPx: Float,
    safeHeightPx: Float,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val (halfWidthPx, halfHeightPx) = CommandHexBounds.halfExtentsPx(dimensions, density)

    var dragOffsetPx by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var didDrag by remember { mutableStateOf(false) }

    val restingCenter = CommandHexBounds.centerFromFraction(
        position = position,
        safeLeftPx = safeLeftPx,
        safeTopPx = safeTopPx,
        safeWidthPx = safeWidthPx,
        safeHeightPx = safeHeightPx,
        halfWidthPx = halfWidthPx,
        halfHeightPx = halfHeightPx,
    )
    val restingCenterState = rememberUpdatedState(restingCenter)

    val scale by animateFloatAsState(
        targetValue = when {
            isDragging -> 1.05f
            isMenuOpen -> 1.03f
            else -> 1f
        },
        label = "orbScale",
    )

    fun clampCenter(raw: Offset): Offset {
        val minX = safeLeftPx + halfWidthPx
        val minY = safeTopPx + halfHeightPx
        val maxX = minX + (safeWidthPx - halfWidthPx * 2f).coerceAtLeast(0f)
        val maxY = minY + (safeHeightPx - halfHeightPx * 2f).coerceAtLeast(0f)
        return Offset(
            x = raw.x.coerceIn(minX, maxX),
            y = raw.y.coerceIn(minY, maxY),
        )
    }

    fun clampDragOffset(anchor: Offset, offset: Offset): Offset {
        val clamped = clampCenter(anchor + offset)
        return clamped - anchor
    }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    (restingCenter.x - halfWidthPx).roundToInt(),
                    (restingCenter.y - halfHeightPx).roundToInt(),
                )
            }
            .graphicsLayer {
                translationX = dragOffsetPx.x
                translationY = dragOffsetPx.y
                scaleX = scale
                scaleY = scale
            }
            .size(dimensions.width, dimensions.height)
            .alpha(opacity)
            .semantics { contentDescription = "Open Field Terminal menu" }
            .clip(OctagonShape)
            .background(PanelDark.copy(alpha = 0.94f))
            .border(2.dp, if (isMenuOpen) SignalCyan else PanelBorder, OctagonShape)
            .pointerInput(dimensions, safeLeftPx, safeTopPx, safeWidthPx, safeHeightPx) {
                var dragAnchor = Offset.Zero
                detectDragGestures(
                    onDragStart = {
                        didDrag = false
                        dragAnchor = restingCenterState.value
                        dragOffsetPx = Offset.Zero
                        isDragging = true
                        onDragStateChange(true)
                    },
                    onDragEnd = {
                        val finalCenter = clampCenter(dragAnchor + dragOffsetPx)
                        val finalPosition = CommandHexBounds.fractionFromCenter(
                            centerX = finalCenter.x,
                            centerY = finalCenter.y,
                            safeLeftPx = safeLeftPx,
                            safeTopPx = safeTopPx,
                            safeWidthPx = safeWidthPx,
                            safeHeightPx = safeHeightPx,
                            halfWidthPx = halfWidthPx,
                            halfHeightPx = halfHeightPx,
                        )
                        isDragging = false
                        onDragStateChange(false)
                        onDragEnd(finalPosition)
                        dragOffsetPx = Offset.Zero
                    },
                    onDragCancel = {
                        dragOffsetPx = Offset.Zero
                        isDragging = false
                        onDragStateChange(false)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffsetPx = clampDragOffset(dragAnchor, dragOffsetPx + dragAmount)
                        if (!didDrag && dragOffsetPx.getDistance() > 12f) {
                            didDrag = true
                        }
                    },
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress() },
                    onTap = {
                        if (!didDrag) onTap()
                        didDrag = false
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Terminal,
            contentDescription = null,
            tint = if (isMenuOpen) SignalCyan else TerminalGreen,
            modifier = Modifier.size(dimensions.width * 0.36f),
        )
    }
}
