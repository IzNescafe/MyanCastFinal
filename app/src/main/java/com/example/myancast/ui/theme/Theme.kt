// ui/theme/Theme.kt
package com.example.myancast.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ─── App Config ─────────────────────────────────
data class AppConfig(
    val zawgyi: Boolean = false,
    val darkMode: Boolean = true
) {
    companion object {
        /** Screen လှည့်တဲ့အခါ setting မပျောက်အောင် သိမ်းပေးတယ်။ */
        val Saver: Saver<AppConfig, Any> = listSaver<AppConfig, Boolean>(
            save = { listOf(it.zawgyi, it.darkMode) },
            restore = { AppConfig(zawgyi = it[0], darkMode = it[1]) }
        )
    }
}

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }

// ─── Dark Scheme ────────────────────────────────
private val DarkColors = darkColorScheme(
    primary              = GoldPrimary,
    onPrimary            = Color(0xFF1A1408),
    primaryContainer     = GoldDark,
    onPrimaryContainer   = TextHi,

    secondary            = TealSecondary,
    onSecondary          = Color(0xFF08201D),

    tertiary             = OkGreen,
    onTertiary           = Color(0xFF062A18),

    error                = LiveRed,
    onError              = TextHi,

    background           = BgDark,
    onBackground         = TextHi,

    surface              = SurfaceDark,
    onSurface            = TextHi,

    surfaceVariant       = Surface2Dark,
    onSurfaceVariant     = TextLo,

    outline              = OutlineDark,
    outlineVariant       = OutlineDark.copy(alpha = 0.5f),

    inverseSurface       = TextHi,
    inverseOnSurface     = BgDark,
    inversePrimary       = GoldDark
)

// ─── Light Scheme (Gold-accent) ─────────────────
private val LightColors = lightColorScheme(
    primary              = GoldDark,
    onPrimary            = Color.White,
    primaryContainer     = GoldPrimary,
    onPrimaryContainer   = Color(0xFF1A1408),

    secondary            = TealSecondary,
    onSecondary          = Color.White,

    tertiary             = OkGreen,
    onTertiary           = Color.White,

    error                = LiveRed,
    onError              = Color.White,

    background           = BgLight,
    onBackground         = TextHiLight,

    surface              = SurfaceLight,
    onSurface            = TextHiLight,

    surfaceVariant       = Color(0xFFF3EDDE),
    onSurfaceVariant     = TextLoLight,

    outline              = OutlineLight
)

@Composable
fun MyanCastTheme(
    config: AppConfig = AppConfig(),
    content: @Composable () -> Unit
) {
    val colors = if (config.darkMode) DarkColors else LightColors
    val typography = myanCastTypography(config.zawgyi)

    CompositionLocalProvider(LocalAppConfig provides config) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
}