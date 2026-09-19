package com.example.myancast.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun SearchScreen(
    onPodcastClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "ရှာဖွေ",
        message = "ရှာဖွေမှုကို Phase 3 မှာ ထည့်မယ်",
        modifier = modifier
    )
}
