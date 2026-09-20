package com.example.myancast.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.example.myancast.ui.components.CategoryChips
import com.example.myancast.ui.components.ContinueListeningCard
import com.example.myancast.ui.components.EmptyView
import com.example.myancast.ui.components.ErrorView
import com.example.myancast.ui.components.LoadingView
import com.example.myancast.ui.components.PodcastCard
import com.example.myancast.ui.components.PodcastListItem
import com.example.myancast.ui.components.SectionHeader
import com.example.myancast.ui.theme.AppConfig
import com.example.myancast.ui.theme.MyanCastTheme
import com.example.myancast.ui.theme.TextHi
import com.example.myancast.ui.theme.TextLo

// ═══════════════════════════════════════════════
// SCREEN
// ═══════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPodcastClick: (String) -> Unit,
    onSettingsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    vm: HomeViewModel = viewModel(factory = HomeViewModel.factory())   // ← ★ factory
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "MyanCast",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHi
                    )
                },
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = TextLo
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = TextLo
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        when {
            // ၁။ Loading အရင်
            state.isLoading -> {
                LoadingView(Modifier.padding(padding))
            }
            // ၂။ Error
            state.error != null -> {
                ErrorView(
                    message = state.error ?: "Unknown error",
                    onRetry = vm::refresh,
                    modifier = Modifier.padding(padding)
                )
            }
            // ၃။ Empty (အသစ်)
            state.podcasts.isEmpty() -> {
                EmptyView(
                    message = "Podcast မရှိသေးပါ",
                    modifier = Modifier.padding(padding)
                )
            }
            // ၄။ Content
            else -> {
                HomeContent(
                    state = state,
                    onCategorySelect = vm::selectCategory,
                    onPodcastClick = onPodcastClick,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════
// CONTENT
// ═══════════════════════════════════════════════
@Composable
private fun HomeContent(
    state: HomeUiState,
    onCategorySelect: (String) -> Unit,
    onPodcastClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // ─── Greeting ───
        item {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "မင်္ဂလာပါ 👋",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextHi
                )
                Text(
                    text = "ဒီနေ့ ဘာနားထောင်မလဲ?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextLo
                )
            }
        }

        // ─── Continue Listening ───
        state.lastPlayed?.let { episode ->
            item {
                Spacer(Modifier.height(12.dp))
                ContinueListeningCard(
                    episode = episode,
                    onClick = { onPodcastClick(episode.podcastId) }
                )
                Spacer(Modifier.height(20.dp))
            }
        }

        // ─── Categories ───
        item {
            Text(
                text = "အမျိုးအစားများ",
                style = MaterialTheme.typography.titleMedium,
                color = TextHi,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        item {
            CategoryChips(
                categories = state.categories,
                selected = state.selectedCategory,
                onSelect = onCategorySelect
            )
            Spacer(Modifier.height(16.dp))
        }

        // ─── Trending Now ───
        if (state.trending.isNotEmpty()) {
            item {
                SectionHeader(title = "လူကြိုက်များနေသည်")   // ← onSeeAll ဖျက်
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.trending) { podcast ->
                        PodcastCard(
                            podcast = podcast,
                            onClick = { onPodcastClick(podcast.id) }
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }

        // ─── All Podcasts ───
        item {
            SectionHeader(title = "Podcast အားလုံး")
        }
        items(state.podcasts) { podcast ->
            PodcastListItem(
                podcast = podcast,
                onClick = { onPodcastClick(podcast.id) }
            )
        }
    }
}

// ═══════════════════════════════════════════════
// PREVIEW
// ═══════════════════════════════════════════════
@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun HomeContentPreview() {
    MyanCastTheme(config = AppConfig(darkMode = true, zawgyi = false)) {
        HomeContent(
            state = HomeUiState(
                podcasts = listOf(
                    Podcast("1", "နည်းပညာနှင့် လူငယ်", "desc", "https://picsum.photos/400", "နည်းပညာ", 12),
                    Podcast("2", "မြန်မာ့သမိုင်း", "desc", "https://picsum.photos/401", "ဇာတ်လမ်း", 8)
                ),
                trending = listOf(
                    Podcast("3", "Myanmar Tech", "desc", "https://picsum.photos/402", "နည်းပညာ", 20)
                ),
                categories = listOf("အားလုံး", "သတင်း", "နည်းပညာ"),
                selectedCategory = "အားလုံး",
                isLoading = false
            ),
            onCategorySelect = {},
            onPodcastClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun HomeLoadingPreview() {
    MyanCastTheme {
        LoadingView()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun HomeEmptyPreview() {
    MyanCastTheme {
        EmptyView(message = "Podcast မရှိသေးပါ")
    }
}