package com.echo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF72BBFF),
    onPrimary = Color(0xFF1B1B1C),
    secondary = Color(0xFF72BBFF),
    onSecondary = Color(0xFF1B1B1C),
    background = Color(0xFF161617),
    onBackground = Color.White,
    surface = Color(0xFF161617),
    onSurface = Color.White,
    surfaceContainer = Color(0xFF1F1F20),
    surfaceVariant = Color(0xFF1F1F20),
    error = Color(0xFFDE1212)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF72BBFF),
    onPrimary = Color(0xFF1B1B1C),
    secondary = Color(0xFF72BBFF),
    onSecondary = Color(0xFF1B1B1C),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF161617),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF161617),
    error = Color(0xFFDE1212)
)

@Composable
fun EchoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}