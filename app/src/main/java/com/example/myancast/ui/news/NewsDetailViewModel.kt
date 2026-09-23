// ui/news/NewsDetailViewModel.kt
package com.example.myancast.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.MyanCastApp
import com.example.myancast.data.repository.FirestoreNewsRepository
import com.example.myancast.data.repository.NewsRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.NewsItem
import com.example.myancast.player.PlayerController
import com.example.myancast.player.PlayerQueue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ═══════════════════════════════════════════════
// UI STATE
// ═══════════════════════════════════════════════
data class NewsDetailUiState(
    val news: NewsItem? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val canRetry: Boolean = false,
    val playError: String? = null
) {
    /** audio မရှိရင် ခလုတ် လုံးဝ မပြဘူး (Phase 2 ရဲ့ "See all" သင်ခန်းစာ) */
    val hasAudio: Boolean get() = !news?.audioUrl.isNullOrBlank()
}

// ═══════════════════════════════════════════════
// VIEWMODEL
// ═══════════════════════════════════════════════
class NewsDetailViewModel(
    private val newsId: String,
    private val repo: NewsRepository,
    private val player: PlayerController
) : ViewModel() {

    private val _state = MutableStateFlow(NewsDetailUiState())
    val state: StateFlow<NewsDetailUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    // ─── Public API ─────────────────────────────

    fun retry() {
        _state.update { it.copy(isLoading = true, error = null, canRetry = false) }
        load()
    }

    /**
     * သတင်း audio ကို episode တစ်ခု အဖြစ် ပြောင်းပြီး Phase 3 ရဲ့ player နဲ့ ဖွင့်တယ် —
     * player က `Episode` ပဲ သိလို့ `NewsItem` ကို တိုက်ရိုက် မပေးနိုင်ဘူး။
     * @return ဖွင့်လို့ရရင် `true` — caller က Player screen ဖွင့်ဖို့
     */
    fun playAudio(): Boolean {
        val episode = _state.value.news?.toEpisode()
        val queue = if (episode == null) PlayerQueue.EMPTY
        else PlayerQueue.from(listOf(episode), episode.id)

        if (queue.isEmpty) {
            _state.update { it.copy(playError = "ဒီသတင်းကို ဖွင့်လို့ မရပါ") }
            return false
        }
        player.play(queue, PODCAST_TITLE_NEWS)
        return true
    }

    fun playErrorShown() {
        _state.update { it.copy(playError = null) }
    }

    // ─── Private ────────────────────────────────

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val news = try {
                repo.getNewsItem(newsId)
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
                return@launch
            }

            if (news == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "သတင်း ရှာမတွေ့ပါ",
                        canRetry = false
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(news = news, isLoading = false, error = null, canRetry = false)
            }
        }
    }

    // ─── Factory ────────────────────────────────

    companion object {
        const val PODCAST_TITLE_NEWS = "သတင်း"

        fun factory(
            newsId: String,
            repo: NewsRepository = FirestoreNewsRepository()
        ) = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyanCastApp
                NewsDetailViewModel(newsId, repo, app.playerController)
            }
        }
    }
}

/** `NewsItem` → `Episode` — player ရဲ့ contract နဲ့ ချိတ်ဖို့ mapping (pure) */
fun NewsItem.toEpisode(): Episode = Episode(
    id = id,
    podcastId = "",                      // သတင်းက podcast အောက် မဟုတ်ဘူး
    title = headline,
    description = body,
    audioUrl = audioUrl.orEmpty(),       // အလွတ်ဆို PlayerQueue က ဖယ်လိုက်မယ်
    coverUrl = imageUrl,
    publishedAt = publishedAt
)
