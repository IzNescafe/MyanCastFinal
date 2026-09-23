package com.example.myancast.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.MyanCastApp
import com.example.myancast.data.repository.LibraryRepository
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.data.repository.PodcastRepositoryImpl
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.example.myancast.player.PlayerController
import com.example.myancast.player.PlayerQueue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// ─────────────────────────────────────────────
// ၁။ UI State
// ─────────────────────────────────────────────

data class PodcastDetailUiState(
    val podcast  : Podcast?      = null,
    val episodes : List<Episode> = emptyList(),
    val isLoading: Boolean       = true,
    val error    : String?       = null,
    val canRetry : Boolean       = false,
    val playError: String?       = null
) {
    val hasContent: Boolean
        get() = podcast != null || episodes.isNotEmpty()
}


// ─────────────────────────────────────────────
// ၂။ ViewModel
// ─────────────────────────────────────────────

class PodcastDetailViewModel(
    private val podcastId: String,
    private val repo: PodcastRepository,
    private val player: PlayerController,
    private val libraryRepo: LibraryRepository
) : ViewModel() {

    // ──────── State ────────

    private val _state = MutableStateFlow(PodcastDetailUiState())
    val state: StateFlow<PodcastDetailUiState> = _state.asStateFlow()

    val nowPlayingId: StateFlow<String?> = player.state
        .map { if (it.isPlaying) it.currentEpisode?.id else null }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // ★ အသစ် — isSubscribed (Flow)
    val isSubscribed: StateFlow<Boolean> = libraryRepo.subscribedIds()
        .map { podcastId in it }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    // ──────── Job Management ────────

    private var loadJob: Job? = null

    // ──────── Init ────────

    init {
        load()
    }

    // ──────── Public API ────────

    fun retry() {
        _state.update { it.copy(isLoading = true, error = null, canRetry = false) }
        load()
    }

    fun playEpisode(episodeId: String): Boolean {
        val queue = PlayerQueue.from(_state.value.episodes, episodeId)
        if (queue.isEmpty) {
            _state.update { it.copy(playError = "ဒီအပိုင်းကို ဖွင့်လို့ မရပါ") }
            return false
        }
        player.play(queue, _state.value.podcast?.title.orEmpty())
        return true
    }

    fun playAll(): Boolean {
        val queue = PlayerQueue.from(_state.value.episodes, null)
        if (queue.isEmpty) {
            _state.update { it.copy(playError = "ဖွင့်လို့ရတဲ့ အပိုင်း မရှိပါ") }
            return false
        }
        player.play(queue, _state.value.podcast?.title.orEmpty())
        return true
    }

    fun playErrorShown() {
        _state.update { it.copy(playError = null) }
    }

    // ★ အသစ် — Subscribe toggle
    fun toggleSubscribe() {
        viewModelScope.launch {
            if (isSubscribed.value) {
                libraryRepo.unsubscribe(podcastId)
            } else {
                libraryRepo.subscribe(podcastId)
            }
        }
    }

    // ──────── Private ────────

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (loadPodcast()) loadEpisodes()
        }
    }

    private suspend fun loadPodcast(): Boolean {
        val podcast = try {
            repo.getPodcast(podcastId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "အင်တာနက် ချိတ်ဆက်မှု မရပါ",
                    canRetry = true
                )
            }
            return false
        }

        if (podcast == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Podcast ရှာမတွေ့ပါ",
                    canRetry = false
                )
            }
            return false
        }

        _state.update { it.copy(podcast = podcast) }
        return true
    }

    private suspend fun loadEpisodes() {
        repo.getEpisodes(podcastId)
            .catch { _ ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "ပိုင်းများ ဆွဲရာမှာ အမှားဖြစ်ခဲ့သည်",
                        canRetry = true
                    )
                }
            }
            .collect { episodes ->
                val cover = _state.value.podcast?.coverUrl.orEmpty()
                val withCover = episodes.map { ep ->
                    if (ep.coverUrl.isBlank()) ep.copy(coverUrl = cover)
                    else ep
                }
                _state.update {
                    it.copy(
                        episodes = withCover,
                        isLoading = false,
                        error = null,
                        canRetry = false
                    )
                }
            }
    }

    // ──────── Factory ────────

    companion object {
        fun factory(
            podcastId: String,
            repo: PodcastRepository = PodcastRepositoryImpl()
        ) = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyanCastApp
                PodcastDetailViewModel(
                    podcastId,
                    repo,
                    app.playerController,
                    app.libraryRepository     // ← ★ ဒါ ထည့်
                )
            }
        }
    }
}