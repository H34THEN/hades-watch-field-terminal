package com.heathen.hadeswatch.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HadesDarkColorScheme = darkColorScheme(
    primary = TerminalGreen,
    onPrimary = UnderworldBlack,
    secondary = SignalCyan,
    onSecondary = UnderworldBlack,
    tertiary = OracularViolet,
    onTertiary = BoneWhite,
    background = UnderworldBlack,
    onBackground = BoneWhite,
    surface = PanelDark,
    onSurface = BoneWhite,
    surfaceVariant = PanelBorder,
    onSurfaceVariant = MutedText,
    error = PomegranateRed,
    onError = BoneWhite,
    outline = PanelBorder,
)

@Composable
fun HadesWatchTheme(
    highContrast: Boolean = false,
    largeText: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (highContrast) {
        HadesDarkColorScheme.copy(
            primary = Color(0xFF00FF88),
            onBackground = Color.White,
            onSurface = Color.White,
            surface = Color(0xFF000000),
            background = Color(0xFF000000),
        )
    } else {
        HadesDarkColorScheme
    }

    val typography = if (largeText) {
        HadesTypography.copy(
            bodyLarge = HadesTypography.bodyLarge.copy(fontSize = HadesTypography.bodyLarge.fontSize * 1.2f),
            bodyMedium = HadesTypography.bodyMedium.copy(fontSize = HadesTypography.bodyMedium.fontSize * 1.2f),
            titleMedium = HadesTypography.titleMedium.copy(fontSize = HadesTypography.titleMedium.fontSize * 1.15f),
        )
    } else {
        HadesTypography
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}
