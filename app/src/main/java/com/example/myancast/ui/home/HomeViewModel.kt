// ui/home/HomeViewModel.kt
package com.example.myancast.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ═══════════════════════════════════════════════
// UI STATE
// ═══════════════════════════════════════════════
data class HomeUiState(
    val allPodcasts: List<Podcast> = emptyList(),
    val podcasts: List<Podcast> = emptyList(),
    val trending: List<Podcast> = emptyList(),
    val categories: List<String> = listOf(ALL_CATEGORY),
    val selectedCategory: String = ALL_CATEGORY,
    val lastPlayed: Episode? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

// ═══════════════════════════════════════════════
// VIEWMODEL
// ═══════════════════════════════════════════════
class HomeViewModel(
    private val repo: PodcastRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadPodcasts()
    }

    // ─── Load ───────────────────────────────────
    private fun loadPodcasts() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            repo.getPodcasts()
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Data ဆွဲမရပါ"
                        )
                    }
                }
                .collect { list ->
                    _state.update { current ->
                        current.copy(
                            allPodcasts = list,
                            trending = list.take(5),
                            categories = buildCategories(list),
                            podcasts = filterByCategory(list, current.selectedCategory),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    // ─── Actions ────────────────────────────────
    fun selectCategory(category: String) {
        _state.update {
            it.copy(
                selectedCategory = category,
                podcasts = filterByCategory(it.allPodcasts, category)
            )
        }
    }

    fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        loadPodcasts()
    }

    // ─── Factory ────────────────────────────────
    companion object {
        fun factory(repo: PodcastRepository = PodcastRepository()) = viewModelFactory {
            initializer { HomeViewModel(repo) }
        }
    }
}