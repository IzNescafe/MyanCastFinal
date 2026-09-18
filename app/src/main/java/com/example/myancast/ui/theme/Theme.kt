// ui/theme/Theme.kt
package com.example.myancast.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

data class AppConfig(
    val zawgyi: Boolean = false,
    val darkMode: Boolean = true
)

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }

private val DarkColors = darkColorScheme(
    primary        = GoldPrimary,
    onPrimary      = Color(0xFF1A1408),
    secondary      = TealSecondary,
    background     = BgDark,
    surface        = SurfaceDark,
    surfaceVariant = Surface2Dark,
    outline        = OutlineDark,
    onBackground   = TextHi,
    onSurface      = TextHi
)

@Composable
fun MyanCastTheme(
    config: AppConfig = AppConfig(),
    content: @Composable () -> Unit
) {
    val colors = if (config.darkMode) DarkColors else lightColorScheme()
    val typography = myanCastTypography(config.zawgyi)

    CompositionLocalProvider(LocalAppConfig provides config) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
}