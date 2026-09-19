package com.example.myancast.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun PodcastDetailScreen(
    podcastId: String,
    onBack: () -> Unit,
    onEpisodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        title = "Podcast အသေးစိတ်",
        message = "podcastId = $podcastId\n\nEpisode စာရင်းကို Phase 2 မှာ ထည့်မယ်",
        modifier = modifier,
        onBack = onBack
    )
}
