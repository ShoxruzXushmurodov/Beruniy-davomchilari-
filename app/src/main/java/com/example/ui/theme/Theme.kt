package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = BrandDarkBlue,
    onPrimaryContainer = Color.White,
    secondary = AccentOrange,
    onSecondary = Color.Black,
    secondaryContainer = DarkCardSecondary,
    onSecondaryContainer = DarkText,
    tertiary = AccentGold,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkCard,
    onSurface = DarkText,
    surfaceVariant = DarkCardSecondary,
    onSurfaceVariant = DarkTextMuted,
    error = SemanticError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEFFF),
    onPrimaryContainer = BrandDarkBlue,
    secondary = AccentOrange,
    onSecondary = Color.White,
    secondaryContainer = LightCardSecondary,
    onSecondaryContainer = LightText,
    tertiary = AccentGold,
    onTertiary = Color.Black,
    background = LightBackground,
    onBackground = LightText,
    surface = LightCard,
    onSurface = LightText,
    surfaceVariant = LightCardSecondary,
    onSurfaceVariant = LightTextMuted,
    error = SemanticError,
    onError = Color.White
)

@Composable
fun BeruniyTheme(
    darkTheme: Boolean = true, // Default dark as requested: "Asosiy tema qorong'i (dark)"
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
