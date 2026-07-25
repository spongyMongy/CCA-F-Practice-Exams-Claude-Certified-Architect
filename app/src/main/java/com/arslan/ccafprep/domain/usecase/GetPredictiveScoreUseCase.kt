package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.domain.model.DomainProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ScorePrediction(
    val score: Int, // 0-100
    val confidence: Float, // 0.0-1.0
    val status: String
)

class GetPredictiveScoreUseCase @Inject constructor(
    private val getProgressSummaryUseCase: GetProgressSummaryUseCase
) {
    operator fun invoke(): Flow<ScorePrediction> {
        return getProgressSummaryUseCase().map { summary ->
            if (summary.isEmpty() || summary.all { it.questionsAttempted == 0 }) {
                return@map ScorePrediction(0, 0f, "Insufficient Data")
            }

            // Weighted calculation based on exam domain weights
            val weightedScore = summary.sumOf { (it.accuracy * it.domain.weight).toDouble() }.toFloat() * 100
            
            // Confidence based on total coverage
            val totalCoverage = summary.map { it.questionsAttempted.toFloat() / it.totalQuestionsInDomain.coerceAtLeast(1) }.average().toFloat()
            
            val status = when {
                weightedScore >= 80 && totalCoverage >= 0.7f -> "Ready for Exam"
                weightedScore >= 65 -> "Getting Closer"
                else -> "Significant Study Required"
            }

            ScorePrediction(weightedScore.toInt(), totalCoverage, status)
        }
    }
}
