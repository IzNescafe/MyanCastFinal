// ui/theme/Type.kt
package com.example.myancast.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myancast.R

val PadaukFamily = FontFamily(
    Font(R.font.padauk_regular, FontWeight.Normal),
    Font(R.font.padauk_bold,    FontWeight.Bold)
)

val ZawgyiFamily = FontFamily(
    Font(R.font.zawgyi_one, FontWeight.Normal),
    Font(R.font.zawgyi_one, FontWeight.Bold)
)

fun myanCastTypography(zawgyi: Boolean): Typography {
    val mm = if (zawgyi) ZawgyiFamily else PadaukFamily
    return Typography(
        displayMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 32.sp, lineHeight = 45.sp
        ),
        titleLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 22.sp, lineHeight = 35.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 16.sp, lineHeight = 29.sp   // line-height 1.8
        ),
        labelMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 13.sp, lineHeight = 21.sp
        )
    )
}