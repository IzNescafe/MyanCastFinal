package com.example.myancast.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.example.myancast.ui.components.EmptyView
import com.example.myancast.ui.components.EpisodeRow
import com.example.myancast.ui.components.ErrorView
import com.example.myancast.ui.components.LoadingView
import com.example.myancast.ui.components.SectionHeader
import com.example.myancast.ui.theme.MyanCastTheme


// ─────────────────────────────────────────────
// ၁။ Public Entry Point — NavHost က ဒါကို ခေါ်
// ─────────────────────────────────────────────

@Composable
fun PodcastDetailScreen(
    podcastId: String,
    onBack: () -> Unit,
    onEpisodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // ViewModel ကို factory နဲ့ ဖန်တီး — podcastId ကို ပေး
    val vm: PodcastDetailViewModel = viewModel(
        factory = PodcastDetailViewModel.factory(podcastId)
    )

    // State ကို collect (StateFlow → Compose State)
    val state by vm.state.collectAsStateWithLifecycle()
    val nowPlayingId by vm.nowPlayingId.collectAsStateWithLifecycle()

    // Stateless content ကို ခေါ်
    PodcastDetailContent(
        state = state,
        nowPlayingId = nowPlayingId,
        onBack = onBack,
        // ဖွင့်လို့ရမှ Player ကို ဖွင့် — audio မရှိရင် snackbar ပဲ ပြ
        onEpisodeClick = { id -> if (vm.playEpisode(id)) onEpisodeClick(id) },
        onPlayAll = { vm.playAll() },
        onRetry = vm::retry,
        onPlayErrorShown = vm::playErrorShown,
        modifier = modifier
    )
}


// ─────────────────────────────────────────────
// ၂။ Stateful Shell — Scaffold + State ခွဲခြား
// ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PodcastDetailContent(
    state: PodcastDetailUiState,
    onBack: () -> Unit,
    onEpisodeClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    nowPlayingId: String? = null,
    onPlayAll: () -> Unit = {},
    onPlayErrorShown: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // playError တစ်ခါ ပြပြီး ပြန်ရှင်း
    LaunchedEffect(state.playError) {
        val message = state.playError ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        onPlayErrorShown()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "နောက်သို့"
                        )
                    }
                },
                // Transparent — cover image နဲ့ ရောသွားအောင်
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        // ⚠️ State အစီအစဉ် — loading → error → podcast null → content
        when {
            state.isLoading -> {
                LoadingView(Modifier.padding(padding))
            }

            // Retry လုပ်လို့ရတဲ့ error (network) — retry ခလုတ်နဲ့
            state.error != null && state.canRetry -> {
                ErrorView(
                    message = state.error,
                    onRetry = onRetry,
                    modifier = Modifier.padding(padding)
                )
            }

            // Retry မရ (ဥပမာ Podcast ရှာမတွေ့ပါ) — message ပဲ ပြ
            state.error != null -> {
                EmptyView(
                    message = state.error,
                    modifier = Modifier.padding(padding)
                )
            }

            state.podcast == null -> {
                EmptyView(
                    message = "Podcast ရှာမတွေ့ပါ",
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                // Local val ခံ — smart cast အတွက်
                val podcast = state.podcast
                DetailContent(
                    podcast = podcast,
                    episodes = state.episodes,
                    onEpisodeClick = onEpisodeClick,
                    modifier = Modifier.padding(padding),
                    nowPlayingId = nowPlayingId,
                    onPlayAll = onPlayAll
                )
            }
        }
    }
}


// ─────────────────────────────────────────────
// ၃။ Content — LazyColumn
// ─────────────────────────────────────────────

@Composable
private fun DetailContent(
    podcast: Podcast,
    episodes: List<Episode>,
    onEpisodeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    nowPlayingId: String? = null,
    onPlayAll: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        // MiniPlayer နေရာကို NavHost ရဲ့ Scaffold padding က ပေးပြီးသား — ဒီမှာက အခွာ အနည်းငယ်ပဲ
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // (၁) Podcast header (cover + title + category + description)
        item {
            PodcastHeader(podcast = podcast)
        }

        // (၂) Action buttons
        item {
            ActionButtons(
                onPlayAll = onPlayAll,
                onSave = { /* TODO */ }
            )
        }

        // (၃) Section header — "ပိုင်းများ (N)"
        item {
            SectionHeader(title = "ပိုင်းများ (${episodes.size})")
        }

        // (၄) Episodes — ရှိရင် row ပြ၊ မရှိရင် EmptyView (screen တစ်ခုလုံး မဟုတ်)
        if (episodes.isEmpty()) {
            item {
                EmptyView("ပိုင်း မရှိသေးပါ")
            }
        } else {
            itemsIndexed(episodes) { i, ep ->
                EpisodeRow(
                    // ⚠️ index က 1 ကနေ စရမယ်
                    index = i + 1,
                    episode = ep,
                    onPlay = { onEpisodeClick(ep.id) },
                    isPlaying = ep.id == nowPlayingId
                )
            }
        }
    }
}


// ─────────────────────────────────────────────
// ၄။ PodcastHeader — cover + title + meta + description
// ─────────────────────────────────────────────

@Composable
private fun PodcastHeader(
    podcast: Podcast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cover
        AsyncImage(
            model = podcast.coverUrl,
            contentDescription = podcast.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(16.dp))

        // Title
        Text(
            text = podcast.title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))

        // Category · N ပိုင်း
        Text(
            text = "${podcast.category} · ${podcast.episodeCount} ပိုင်း",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        // Description (max 3 ကြောင်း)
        Text(
            text = podcast.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
    }
}


// ─────────────────────────────────────────────
// ၅။ ActionButtons — အားလုံး ဖွင့် + သိမ်း
// ─────────────────────────────────────────────

@Composable
private fun ActionButtons(
    onPlayAll: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPlayAll,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("အားလုံး ဖွင့်")
        }

        OutlinedButton(onClick = onSave) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("သိမ်း")
        }
    }
}


// ─────────────────────────────────────────────
// ၆။ @Preview — DetailContent ကိုသာ preview
// ─────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun PodcastDetailContentPreview() {
    MyanCastTheme {
        PodcastDetailContent(
            state = PodcastDetailUiState(
                podcast = fakePodcast,
                episodes = fakeEpisodes,
                isLoading = false
            ),
            onBack = {},
            onEpisodeClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun PodcastDetailNotFoundPreview() {
    MyanCastTheme {
        PodcastDetailContent(
            state = PodcastDetailUiState(isLoading = false, error = "Podcast ရှာမတွေ့ပါ"),
            onBack = {},
            onEpisodeClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun DetailContentPreview() {
    MyanCastTheme {
        DetailContent(
            podcast = fakePodcast,
            episodes = fakeEpisodes,
            onEpisodeClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun DetailContentEmptyEpisodesPreview() {
    MyanCastTheme {
        DetailContent(
            podcast = fakePodcast,
            episodes = emptyList(),
            onEpisodeClick = {},
            modifier = Modifier
        )
    }
}


// ─────────────────────────────────────────────
// ၇။ Preview Fake Data
// ─────────────────────────────────────────────

private val fakePodcast = Podcast(
    id = "p1",
    title = "မြန်မာ့ သတင်း အစီအစဉ်",
    description = "နေ့စဉ် မြန်မာ့ သတင်းတွေကို အသံနဲ့ နားထောင်ပါ။ " +
            "နိုင်ငံရေး၊ စီးပွားရေး၊ လူမှုရေး သတင်းများ အပါအဝင်။",
    coverUrl = "https://picsum.photos/seed/p1/400",
    category = "သတင်း",
    episodeCount = 4
)

private val fakeEpisodes = listOf(
    Episode(
        id = "e1",
        title = "အပိုင်း ၁ — မနက်ခင်း သတင်း",
        audioUrl = "https://example.com/e1.mp3",
        podcastId = "p1",
        duration = 1800
    ),
    Episode(
        id = "e2",
        title = "အပိုင်း ၂ — စီးပွားရေး သတင်း",
        audioUrl = "https://example.com/e2.mp3",
        podcastId = "p1",
        duration = 2100
    ),
    Episode(
        id = "e3",
        title = "အပိုင်း ၃ — နိုင်ငံရေး သတင်း",
        audioUrl = "https://example.com/e3.mp3",
        podcastId = "p1",
        duration = 1500
    ),
    Episode(
        id = "e4",
        title = "အပိုင်း ၄ — အားကစား သတင်း",
        audioUrl = "https://example.com/e4.mp3",
        podcastId = "p1",
        duration = 2400
    )
)