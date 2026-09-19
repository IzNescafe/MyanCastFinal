package com.example.myancast.ui.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun NewsScreen(
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "သတင်း",
        message = "သတင်း စာရင်းကို Phase 2 မှာ ထည့်မယ်",
        modifier = modifier
    )
}
