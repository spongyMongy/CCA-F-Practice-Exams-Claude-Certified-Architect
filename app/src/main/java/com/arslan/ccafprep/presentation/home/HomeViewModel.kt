package com.arslan.ccafprep.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.data.local.seed.DatabaseSeeder
import com.arslan.ccafprep.domain.model.DomainProgress
import com.arslan.ccafprep.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val seeder: DatabaseSeeder,
    private val settingsManager: SettingsManager,
    getWeakAreasUseCase: GetWeakAreasUseCase,
    getProgressSummaryUseCase: GetProgressSummaryUseCase,
    getUserStatsUseCase: GetUserStatsUseCase,
    getPredictiveScoreUseCase: GetPredictiveScoreUseCase
) : ViewModel() {

    val isProUnlocked: StateFlow<Boolean> = settingsManager.isProUnlocked
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val weakAreas: StateFlow<List<WeakArea>> = getWeakAreasUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val domainProgress: StateFlow<List<DomainProgress>> = getProgressSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userStats: StateFlow<UserStats> = getUserStatsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStats(0, 0, 0f))

    val forecast: StateFlow<ScorePrediction> = getPredictiveScoreUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScorePrediction(0, 0f, "Analyzing..."))

    val dailyTip: StateFlow<String> = isProUnlocked.map {
        val tips = listOf(
            "Use XML for complex Tool definitions to ensure Claude parses parameters correctly.",
            "Always include a 'description' field for every tool argument to improve agentic accuracy.",
            "Use the System Prompt to enforce 'thinking' blocks for multi-step reasoning.",
            "Prefer 'User-in-the-loop' confirmations for destructive tool actions.",
            "Use MCP to standardize external data retrieval across different Claude interfaces."
        )
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        tips[dayOfYear % tips.size]
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Prepare for the certification with daily architectural insights.")

    val homeUiState: StateFlow<HomeUiState> = combine(
        isProUnlocked,
        weakAreas,
        domainProgress,
        dailyTip,
        userStats,
        forecast
    ) { args ->
        val isPro = args[0] as Boolean
        val weak = args[1] as List<WeakArea>
        val progress = args[2] as List<DomainProgress>
        val tip = args[3] as String
        val stats = args[4] as UserStats
        val fc = args[5] as ScorePrediction

        val overallReadiness = if (progress.isNotEmpty()) progress.map { it.readinessScore }.average().toFloat() else 0f
        
        val milestones = listOf(
            Milestone("Novice", androidx.compose.material.icons.Icons.Default.Person, true),
            Milestone("Orchestrator", androidx.compose.material.icons.Icons.Default.Build, overallReadiness > 0.4f),
            Milestone("Optimizer", androidx.compose.material.icons.Icons.Default.CheckCircle, overallReadiness > 0.7f),
            Milestone("Architect Elite", androidx.compose.material.icons.Icons.Default.Star, isPro)
        )

        HomeUiState(
            isPro = isPro,
            weakAreas = weak,
            domainProgress = progress,
            dailyTip = tip,
            overallReadiness = overallReadiness,
            streak = stats.streak,
            weeklyGoalProgress = stats.weeklyGoalProgress,
            forecast = fc,
            milestones = milestones
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())

    init {
        viewModelScope.launch {
            seeder.seedIfNeeded()
        }
    }

    fun setImmediateFeedback(immediate: Boolean) = viewModelScope.launch {
        settingsManager.setImmediateFeedback(immediate)
    }
}

data class HomeUiState(
    val isPro: Boolean = false,
    val weakAreas: List<WeakArea> = emptyList(),
    val domainProgress: List<DomainProgress> = emptyList(),
    val dailyTip: String = "",
    val overallReadiness: Float = 0f,
    val streak: Int = 0,
    val weeklyGoalProgress: Float = 0f,
    val forecast: ScorePrediction = ScorePrediction(0, 0f, ""),
    val milestones: List<Milestone> = emptyList()
)

data class Milestone(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val isUnlocked: Boolean
)
