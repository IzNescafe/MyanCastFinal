package com.example.myancast.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun FullPlayerScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "ဖွင့်နေသည်",
        message = "Player ကို Phase 3 မှာ ထည့်မယ်",
        modifier = modifier,
        onBack = onBack
    )
}
