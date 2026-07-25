package com.arslan.ccafprep.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.ccafprep.data.local.dao.DailyActivityDao
import com.arslan.ccafprep.domain.model.DomainProgress
import com.arslan.ccafprep.domain.usecase.GetPredictiveScoreUseCase
import com.arslan.ccafprep.domain.usecase.GetProgressSummaryUseCase
import com.arslan.ccafprep.domain.usecase.ScorePrediction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getProgressSummaryUseCase: GetProgressSummaryUseCase,
    private val activityDao: DailyActivityDao,
    private val getPredictiveScoreUseCase: GetPredictiveScoreUseCase
) : ViewModel() {

    val uiState: StateFlow<ProgressUiState> = combine(
        getProgressSummaryUseCase(),
        activityDao.getLast30DaysActivity(),
        getPredictiveScoreUseCase()
    ) { progressList, activity, forecast ->
        val avgReadiness = if (progressList.isNotEmpty()) progressList.map { it.readinessScore }.average().toFloat() else 0f
        
        // Ensure exactly 28 days for the heatmap
        val activityData = List(28) { index ->
            val calendar = java.util.Calendar.getInstance()
            calendar.add(java.util.Calendar.DAY_OF_YEAR, - (27 - index))
            val dateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(calendar.time)
            activity.find { it.date == dateStr }?.count ?: 0
        }

        ProgressUiState(
            domainProgress = progressList,
            overallReadiness = avgReadiness,
            activityData = activityData,
            forecast = forecast,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProgressUiState(isLoading = true)
    )
}

data class ProgressUiState(
    val domainProgress: List<DomainProgress> = emptyList(),
    val overallReadiness: Float = 0f,
    val activityData: List<Int> = emptyList(),
    val forecast: ScorePrediction = ScorePrediction(0, 0f, ""),
    val isLoading: Boolean = false
)
