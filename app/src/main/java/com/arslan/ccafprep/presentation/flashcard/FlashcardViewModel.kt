package com.arslan.ccafprep.presentation.flashcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Flashcard
import com.arslan.ccafprep.domain.usecase.GetFlashcardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val getFlashcardsUseCase: GetFlashcardsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val domainId: Int = savedStateHandle["domainId"] ?: -1

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            val domain = ExamDomain.entries.find { it.id == domainId }
            getFlashcardsUseCase(domain).collectLatest { flashcards ->
                _uiState.update { it.copy(flashcards = flashcards, isLoading = false) }
            }
        }
    }

    fun nextCard() {
        val currentState = _uiState.value
        if (currentState.currentIndex < currentState.flashcards.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1, isFlipped = false) }
        }
    }

    fun flipCard() {
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }
}

data class FlashcardUiState(
    val flashcards: List<Flashcard> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true
)
