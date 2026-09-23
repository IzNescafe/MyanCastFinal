// ui/search/SearchViewModel.kt
package com.example.myancast.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.data.repository.PodcastRepositoryImpl
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// ═══════════════════════════════════════════════
// UI STATE
// ═══════════════════════════════════════════════
data class SearchUiState(
    val query: String = "",
    val results: List<Podcast> = emptyList(),
    val isIdle: Boolean = true,        // မရိုက်ရသေး — "ရှာလိုသည်ကို ရိုက်ပါ"
    val isLoading: Boolean = true,
    val error: String? = null
)

// ═══════════════════════════════════════════════
// VIEWMODEL
// ═══════════════════════════════════════════════
class SearchViewModel(
    repo: PodcastRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")

    /**
     * Query ပြောင်းရင်ရော Firestore data ပြောင်းရင်ရော ပြန်တွက်ဖို့ `combine` သုံးတယ်။
     * Filter logic က pure function ထဲမှာ (composable ထဲ မဟုတ်) — Phase 2 စည်းကမ်း။
     */
    val state: StateFlow<SearchUiState> = combine(
        _query,
        repo.getPodcasts()
    ) { query, podcasts ->
        SearchUiState(
            query = query,
            results = searchPodcasts(podcasts, query),
            isIdle = query.isBlank(),
            isLoading = false,
            error = null
        )
    }
        .catch { e ->
            emit(
                SearchUiState(
                    query = _query.value,
                    isIdle = _query.value.isBlank(),
                    isLoading = false,
                    error = e.message ?: "Data ဆွဲမရပါ"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState()
        )

    // ─── Actions ────────────────────────────────
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun clearQuery() {
        _query.value = ""
    }

    // ─── Factory ────────────────────────────────
    companion object {
        fun factory(
            repo: PodcastRepository = PodcastRepositoryImpl()
        ) = viewModelFactory {
            initializer { SearchViewModel(repo) }
        }
    }
}
