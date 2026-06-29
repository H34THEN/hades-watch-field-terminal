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
    var dragStart by remember { mutableStateOf(Offset.Zero) }
    var dragAccum by remember { mutableStateOf(Offset.Zero) }
    var didDrag by remember { mutableStateOf(false) }

    val centerX = safeLeftPx + position.xFraction * safeWidthPx
    val centerY = safeTopPx + position.yFraction * safeHeightPx

    val scale by animateFloatAsState(
        targetValue = when {
            isDragging -> 1.08f
            isMenuOpen -> 1.04f
            else -> 1f
        },
        label = "hexScale",
    )

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
            .pointerInput(safeWidthPx, safeHeightPx, position) {
                detectDragGestures(
                    onDragStart = {
                        dragStart = Offset(centerX, centerY)
                        dragAccum = Offset.Zero
                        didDrag = false
                        onDragStateChange(true)
                    },
                    onDragEnd = {
                        onDragStateChange(false)
                        dragAccum = Offset.Zero
                    },
                    onDragCancel = {
                        onDragStateChange(false)
                        dragAccum = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragAccum += dragAmount
                        if (dragAccum.getDistance() > 12f) didDrag = true
                        val newX = (dragStart.x + dragAccum.x - safeLeftPx)
                            .coerceIn(hexPx / 2f, safeWidthPx - hexPx / 2f)
                        val newY = (dragStart.y + dragAccum.y - safeTopPx)
                            .coerceIn(hexPx / 2f, safeHeightPx - hexPx / 2f)
                        onPositionChange(
                            HexPosition(
                                xFraction = (newX / safeWidthPx).coerceIn(0f, 1f),
                                yFraction = (newY / safeHeightPx).coerceIn(0f, 1f),
                            ),
                        )
                    },
                )
            }
            .pointerInput(didDrag) {
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
