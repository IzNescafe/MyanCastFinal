package com.example.myancast.ui.details

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.ui.components.PlaceholderScreen

@Composable
fun PodcastDetailScreen(
    podcastId: String,
    onBack: () -> Unit,
    onEpisodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // ────── ယာယီ Test Code (Day 2 မှာ ဖျက်မယ်) ──────
    LaunchedEffect(podcastId) {
        val repo = PodcastRepository()

        Log.d("SLICE_B", "podcastId = $podcastId")

        try {
            val podcast = repo.getPodcast(podcastId)
            Log.d("SLICE_B", "podcast = $podcast")
        } catch (e: Exception) {
            Log.e("SLICE_B", "getPodcast failed", e)
        }

        try {
            repo.getEpisodes(podcastId).collect { episodes ->
                Log.d("SLICE_B", "episodes = ${episodes.size}")
                episodes.forEach {
                    Log.d("SLICE_B", "  - ${it.title}")
                }
            }
        } catch (e: Exception) {
            Log.e("SLICE_B", "getEpisodes failed", e)
        }
    }
    // ──────────────────────────────────────────────

    PlaceholderScreen(
        title = "Podcast အသေးစိတ်",
        message = "podcastId = $podcastId\n\nEpisode စာရင်းကို Phase 2 မှာ ထည့်မယ်",
        modifier = modifier,
        onBack = onBack
    )
}