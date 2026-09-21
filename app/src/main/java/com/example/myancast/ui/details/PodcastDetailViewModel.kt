package com.example.myancast.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    val canRetry : Boolean       = false
) {
    val hasContent: Boolean
        get() = podcast != null || episodes.isNotEmpty()
}


// ─────────────────────────────────────────────
// ၂။ ViewModel
// ─────────────────────────────────────────────

class PodcastDetailViewModel(
    private val podcastId: String,
    private val repo: PodcastRepository
) : ViewModel() {

    // ──────── State ────────

    private val _state = MutableStateFlow(PodcastDetailUiState())
    val state: StateFlow<PodcastDetailUiState> = _state.asStateFlow()

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

    // ──────── Private ────────

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            // Podcast မရှိရင် episodes ဆက်မဆွဲ — မဟုတ်ရင် error ကို ပြန်ဖျက်သွားမယ်
            if (loadPodcast()) loadEpisodes()
        }
    }

    /** @return podcast ရရင် true */
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
                //cover fallback logic
                val cover = _state.value.podcast?.coverUrl.orEmpty()
                //mean cover may be same for podcast and episodes
                val withCover = episodes.map { ep ->
                    if(ep.coverUrl.isBlank()){
                        ep.copy(coverUrl = cover)
                    }   else {
                        ep
                    }
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
            repo: PodcastRepository = PodcastRepository()
        ) = viewModelFactory {
            initializer {
                PodcastDetailViewModel(podcastId, repo)
            }
        }
    }
}