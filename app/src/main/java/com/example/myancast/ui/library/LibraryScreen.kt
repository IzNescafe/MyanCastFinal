package com.example.myancast.ui.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun LibraryScreen(
    onPodcastClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "စာကြည့်တိုက်",
        message = "သိမ်းထားတဲ့ podcast တွေကို Phase 4 မှာ ထည့်မယ်",
        modifier = modifier
    )
}
