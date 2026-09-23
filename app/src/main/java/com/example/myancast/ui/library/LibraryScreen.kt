package com.example.myancast.ui.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myancast.MyanCastApp
import com.example.myancast.data.repository.PodcastRepositoryImpl
import com.example.myancast.domain.model.Podcast
import com.example.myancast.ui.components.EmptyView
import com.example.myancast.ui.components.ErrorView
import com.example.myancast.ui.components.LoadingView
import com.example.myancast.ui.components.PodcastListItem
import com.example.myancast.ui.theme.TextHi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onPodcastClick: (String) -> Unit,
    vm: LibraryViewModel
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "စာကြည့်တိုက်",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHi
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingView(Modifier.padding(padding))
            state.error != null -> ErrorView(
                message = state.error ?: "Error",
                onRetry = { },
                modifier = Modifier.padding(padding)
            )
            else -> LibraryContent(
                state = state,
                onTabSelect = vm::selectTab,
                onPodcastClick = onPodcastClick,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun LibraryContent(
    state: LibraryUiState,
    onTabSelect: (LibraryTab) -> Unit,
    onPodcastClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Tab Row
        TabRow(
            selectedTabIndex = state.tab.ordinal,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Tab(
                selected = state.tab == LibraryTab.SUBSCRIBED,
                onClick = { onTabSelect(LibraryTab.SUBSCRIBED) },
                text = { Text("သိမ်းထားသော") }
            )
            Tab(
                selected = state.tab == LibraryTab.HISTORY,
                onClick = { onTabSelect(LibraryTab.HISTORY) },
                text = { Text("မှတ်တမ်း") }
            )
        }

        // Content
        when (state.tab) {
            LibraryTab.SUBSCRIBED -> SubscribedList(
                podcasts = state.subscribed,
                onPodcastClick = onPodcastClick
            )
            LibraryTab.HISTORY -> HistoryList(
                history = state.history
            )
        }
    }
}

@Composable
private fun SubscribedList(
    podcasts: List<Podcast>,
    onPodcastClick: (String) -> Unit
) {
    if (podcasts.isEmpty()) {
        EmptyView("သိမ်းထားတာ မရှိသေးပါ")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(podcasts) { podcast ->
                PodcastListItem(
                    podcast = podcast,
                    onClick = { onPodcastClick(podcast.id) }
                )
            }
        }
    }
}

@Composable
private fun HistoryList(
    history: List<HistoryRow>
) {
    if (history.isEmpty()) {
        EmptyView("နားထောင်ထားတာ မရှိသေးပါ")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(history) { row ->
                HistoryItem(row = row)
            }
        }
    }
}

@Composable
private fun HistoryItem(row: HistoryRow) {
    // HistoryItem — ရိုးရှင်း
    // (သို့) PodcastListItem ကို wrap လုပ်
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = row.episodeTitle,
            style = MaterialTheme.typography.titleMedium,
            color = TextHi
        )
        Text(
            text = row.podcastTitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // Progress bar
        androidx.compose.material3.LinearProgressIndicator(
            progress = { row.progress.fraction },
            modifier = Modifier
                .padding(top = 8.dp)
                .size(width = 200.dp, height = 3.dp)
        )
    }
}