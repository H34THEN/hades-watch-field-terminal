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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
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

    var dragCenterPx by remember { mutableStateOf<Offset?>(null) }
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
    val center = dragCenterPx ?: restingCenter
    val isDragging = dragCenterPx != null

    val scale by animateFloatAsState(
        targetValue = when {
            isDragging -> 1.06f
            isMenuOpen -> 1.03f
            else -> 1f
        },
        label = "hexScale",
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

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    (center.x - halfWidthPx).roundToInt(),
                    (center.y - halfHeightPx).roundToInt(),
                )
            }
            .size(dimensions.width, dimensions.height)
            .scale(scale)
            .alpha(opacity)
            .semantics { contentDescription = "Open Field Terminal menu" }
            .clip(HexagonShape)
            .background(PanelDark.copy(alpha = 0.94f))
            .border(2.dp, if (isMenuOpen) SignalCyan else PanelBorder, HexagonShape)
            .pointerInput(dimensions, safeLeftPx, safeTopPx, safeWidthPx, safeHeightPx) {
                detectDragGestures(
                    onDragStart = {
                        didDrag = false
                        dragCenterPx = restingCenter
                        onDragStateChange(true)
                    },
                    onDragEnd = {
                        val finalCenter = dragCenterPx ?: restingCenter
                        dragCenterPx = null
                        onDragStateChange(false)
                        onDragEnd(
                            CommandHexBounds.fractionFromCenter(
                                centerX = finalCenter.x,
                                centerY = finalCenter.y,
                                safeLeftPx = safeLeftPx,
                                safeTopPx = safeTopPx,
                                safeWidthPx = safeWidthPx,
                                safeHeightPx = safeHeightPx,
                                halfWidthPx = halfWidthPx,
                                halfHeightPx = halfHeightPx,
                            ),
                        )
                    },
                    onDragCancel = {
                        dragCenterPx = null
                        onDragStateChange(false)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val current = dragCenterPx ?: restingCenter
                        val next = clampCenter(current + dragAmount)
                        dragCenterPx = next
                        if (!didDrag && (next - restingCenter).getDistance() > 12f) {
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
            modifier = Modifier.size(dimensions.width * 0.38f),
        )
    }
}
