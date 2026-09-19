// ui/theme/Type.kt
package com.example.myancast.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myancast.R

// ─── Font Families ──────────────────────────────
val PadaukFamily: FontFamily = FontFamily(
    Font(R.font.padauk_regular, FontWeight.Normal),
    Font(R.font.padauk_bold,    FontWeight.Bold),
)

val ZawgyiFamily: FontFamily = FontFamily(
    Font(R.font.zawgyi_one, FontWeight.Normal)
    // Bold file မရှိ → synthetic bold ကို ခွင့်ပြု
)

val PoppinsFamily: FontFamily = FontFamily(
    Font(R.font.poppins_regular,   FontWeight.Normal),
    Font(R.font.poppins_semibold,  FontWeight.SemiBold),
    Font(R.font.poppins_bold,      FontWeight.Bold)
)

// ─── Typography Builder ─────────────────────────
fun myanCastTypography(zawgyi: Boolean): Typography {
    val mm = if (zawgyi) ZawgyiFamily else PadaukFamily

    return Typography(
        // ── Display (large headings) ──
        displayLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 36.sp, lineHeight = 50.sp, letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 28.sp, lineHeight = 42.sp, letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 24.sp, lineHeight = 36.sp
        ),

        // ── Headline ──
        headlineLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 22.sp, lineHeight = 34.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 20.sp, lineHeight = 30.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp, lineHeight = 28.sp
        ),

        // ── Title ──
        titleLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 18.sp, lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp, lineHeight = 26.sp
        ),
        titleSmall = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Medium,
            fontSize = 14.sp, lineHeight = 22.sp
        ),

        // ── Body (Myanmar: line-height 1.8x) ──
        bodyLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 16.sp, lineHeight = 29.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 14.sp, lineHeight = 25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 12.sp, lineHeight = 22.sp
        ),

        // ── Label (English → Poppins) ──
        labelLarge = TextStyle(
            fontFamily = PoppinsFamily, fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.3.sp
        ),
        labelMedium = TextStyle(
            fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium,
            fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp
        ),
        labelSmall = TextStyle(
            fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium,
            fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp
        )
    )
}