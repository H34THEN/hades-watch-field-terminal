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
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.Dp
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
    hexSize: Dp,
    opacity: Float,
    isMenuOpen: Boolean,
    isDragging: Boolean,
    onPositionChange: (HexPosition) -> Unit,
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
    val hexPx = with(density) { hexSize.toPx() }

    // Local drag offset keeps the gesture alive — parent position must not reset pointerInput.
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var dragStartCenter by remember { mutableStateOf(Offset.Zero) }
    var didDrag by remember { mutableStateOf(false) }

    val baseCenterX = safeLeftPx + position.xFraction * safeWidthPx
    val baseCenterY = safeTopPx + position.yFraction * safeHeightPx
    val centerX = if (isDragging) dragStartCenter.x + dragOffsetX else baseCenterX
    val centerY = if (isDragging) dragStartCenter.y + dragOffsetY else baseCenterY

    val scale by animateFloatAsState(
        targetValue = when {
            isDragging -> 1.08f
            isMenuOpen -> 1.04f
            else -> 1f
        },
        label = "hexScale",
    )

    fun positionFromCenter(cx: Float, cy: Float): HexPosition {
        val newX = (cx - safeLeftPx).coerceIn(hexPx / 2f, safeWidthPx - hexPx / 2f)
        val newY = (cy - safeTopPx).coerceIn(hexPx / 2f, safeHeightPx - hexPx / 2f)
        return HexPosition(
            xFraction = (newX / safeWidthPx).coerceIn(0f, 1f),
            yFraction = (newY / safeHeightPx).coerceIn(0f, 1f),
        )
    }

    Box(
        modifier = modifier
            .offset { IntOffset((centerX - hexPx / 2f).roundToInt(), (centerY - hexPx / 2f).roundToInt()) }
            .size(hexSize)
            .scale(scale)
            .alpha(opacity)
            .semantics { contentDescription = "Open Field Terminal menu" }
            .clip(HexagonShape)
            .background(PanelDark.copy(alpha = 0.94f))
            .border(2.dp, if (isMenuOpen) SignalCyan else PanelBorder, HexagonShape)
            .pointerInput(hexSize, safeLeftPx, safeTopPx, safeWidthPx, safeHeightPx) {
                detectDragGestures(
                    onDragStart = {
                        dragStartCenter = Offset(baseCenterX, baseCenterY)
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                        didDrag = false
                        onDragStateChange(true)
                    },
                    onDragEnd = {
                        val finalPosition = positionFromCenter(
                            dragStartCenter.x + dragOffsetX,
                            dragStartCenter.y + dragOffsetY,
                        )
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                        onDragStateChange(false)
                        onPositionChange(finalPosition)
                        onDragEnd(finalPosition)
                    },
                    onDragCancel = {
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                        onDragStateChange(false)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffsetX += dragAmount.x
                        dragOffsetY += dragAmount.y
                        if (!didDrag && Offset(dragOffsetX, dragOffsetY).getDistance() > 12f) {
                            didDrag = true
                        }
                        val livePosition = positionFromCenter(
                            dragStartCenter.x + dragOffsetX,
                            dragStartCenter.y + dragOffsetY,
                        )
                        onPositionChange(livePosition)
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
            modifier = Modifier.size(hexSize * 0.42f),
        )
    }
}
