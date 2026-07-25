package com.arslan.ccafprep.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Question
import com.arslan.ccafprep.domain.repository.QuestionRepository
import com.arslan.ccafprep.domain.usecase.UpdateProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: Int? = null,
    val isFinished: Boolean = false,
    val showTimer: Boolean = true,
    val immediateFeedback: Boolean = true,
    val elapsedSeconds: Long = 0,
    val bufferedResults: Map<String, Boolean> = emptyMap(),
    val correctCount: Int = 0
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuestionRepository,
    private val updateProgressUseCase: UpdateProgressUseCase,
    private val settingsManager: SettingsManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mode: String = savedStateHandle["mode"] ?: "random"
    private val domainId: Int = savedStateHandle["domainId"] ?: -1

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadQuestions()
        observeSettings()
        startTimer()
    }

    private fun observeSettings() {
        settingsManager.showTimer.onEach { show ->
            _uiState.update { it.copy(showTimer = show) }
        }.launchIn(viewModelScope)

        settingsManager.feedbackMode.onEach { immediate ->
            _uiState.update { it.copy(immediateFeedback = immediate) }
        }.launchIn(viewModelScope)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val questions = when (mode) {
                "domain" -> {
                    val domain = ExamDomain.entries.find { it.id == domainId } ?: ExamDomain.AGENTIC_ARCHITECTURE
                    repository.getQuestionsByDomain(domain).first()
                }
                "mock" -> {
                    // Weighted Mock Exam Selection (60 questions total)
                    // Domain 1: 27% (16 questions)
                    // Domain 2: 18% (11 questions)
                    // Domain 3: 20% (12 questions)
                    // Domain 4: 20% (12 questions)
                    // Domain 5: 15% (9 questions)
                    val all = repository.getAllQuestions().first()
                    val random = if (domainId > 0) kotlin.random.Random(domainId.toLong()) else kotlin.random.Random.Default
                    
                    val d1 = all.filter { it.domain == ExamDomain.AGENTIC_ARCHITECTURE }.shuffled(random).take(16)
                    val d2 = all.filter { it.domain == ExamDomain.TOOL_DESIGN }.shuffled(random).take(11)
                    val d3 = all.filter { it.domain == ExamDomain.CLAUDE_CODE }.shuffled(random).take(12)
                    val d4 = all.filter { it.domain == ExamDomain.PROMPT_ENGINEERING }.shuffled(random).take(12)
                    val d5 = all.filter { it.domain == ExamDomain.CONTEXT_MANAGEMENT }.shuffled(random).take(9)
                    
                    (d1 + d2 + d3 + d4 + d5).shuffled(random)
                }
                else -> repository.getAllQuestions().first().shuffled().take(20)
            }
            _uiState.update { it.copy(questions = questions) }
        }
    }

    fun submitAnswer(answerIndex: Int) {
        val state = _uiState.value
        if (state.isFinished) return

        // In immediate feedback mode, prevent changing the answer after selection
        if (state.immediateFeedback && state.selectedAnswer != null) return

        val currentQuestion = state.questions[state.currentQuestionIndex]
        val isCorrect = answerIndex == currentQuestion.correctIndex

        _uiState.update { it.copy(
            selectedAnswer = answerIndex,
            // Only update correctCount here for immediate mode or first selection
            correctCount = if (isCorrect && state.selectedAnswer == null) it.correctCount + 1 
                          else if (!isCorrect && state.selectedAnswer == currentQuestion.correctIndex) it.correctCount - 1
                          else it.correctCount,
            bufferedResults = it.bufferedResults + (currentQuestion.id to isCorrect)
        ) }

        if (state.immediateFeedback) {
            viewModelScope.launch {
                updateProgressUseCase(currentQuestion.id, isCorrect)
            }
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex < state.questions.size - 1) {
            _uiState.update { 
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                    selectedAnswer = null
                )
            }
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        
        // Final score calculation from buffered results to ensure accuracy
        val finalScore = _uiState.value.bufferedResults.values.count { it }
        
        _uiState.update { it.copy(isFinished = true, correctCount = finalScore) }
        
        if (!_uiState.value.immediateFeedback) {
            viewModelScope.launch {
                _uiState.value.bufferedResults.forEach { (id, correct) ->
                    updateProgressUseCase(id, correct)
                }
            }
        }
    }
}
