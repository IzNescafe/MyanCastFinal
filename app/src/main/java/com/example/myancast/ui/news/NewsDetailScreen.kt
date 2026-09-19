package com.example.myancast.ui.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun NewsDetailScreen(
    newsId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "သတင်း အသေးစိတ်",
        message = "newsId = $newsId\n\nအကြောင်းအရာကို Phase 2 မှာ ထည့်မယ်",
        modifier = modifier,
        onBack = onBack
    )
}
