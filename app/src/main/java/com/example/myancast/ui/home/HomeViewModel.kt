// ui/home/HomeViewModel.kt
package com.example.myancast.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ★ UiState — same file ထဲမှာ ★
data class HomeUiState(
    val podcasts: List<Podcast> = emptyList(),
    val trending: List<Podcast> = emptyList(),
    val categories: List<String> = listOf("All", "News", "Tech", "Story", "Music"),
    val selectedCategory: String = "All",
    val lastPlayed: Episode? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val repo: PodcastRepository = PodcastRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadPodcasts()
    }

    private fun loadPodcasts() {
        viewModelScope.launch {
            repo.getPodcasts()
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { list ->
                    _state.update {
                        it.copy(
                            podcasts = list,
                            trending = list.take(5),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun selectCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        loadPodcasts()
    }
}