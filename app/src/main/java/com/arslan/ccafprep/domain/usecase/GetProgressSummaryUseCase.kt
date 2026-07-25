package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.data.local.dao.ProgressDao
import com.arslan.ccafprep.domain.model.DomainProgress
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetProgressSummaryUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val progressDao: ProgressDao
) {
    operator fun invoke(): Flow<List<DomainProgress>> {
        return combine(
            questionRepository.getAllQuestions(),
            progressDao.getAllProgress()
        ) { allQuestions, allProgress ->
            val progressMap = allProgress.associateBy { it.questionId }
            
            ExamDomain.values().map { domain ->
                val domainQuestions = allQuestions.filter { it.domain == domain }
                val domainAttempts = domainQuestions.mapNotNull { progressMap[it.id] }
                
                val totalInDomain = domainQuestions.size
                val attemptedInDomain = domainAttempts.size
                
                val totalCorrect = domainAttempts.sumOf { it.totalCorrect }
                val totalTries = domainAttempts.sumOf { it.totalAttempts }
                
                val accuracy = if (totalTries > 0) totalCorrect.toFloat() / totalTries else 0f
                
                // Heuristic: Weighted by both accuracy and coverage
                val coverage = if (totalInDomain > 0) attemptedInDomain.toFloat() / totalInDomain else 0f
                val readiness = (accuracy * 0.7f) + (coverage * 0.3f)
                
                DomainProgress(
                    domain = domain,
                    accuracy = accuracy,
                    questionsAttempted = attemptedInDomain,
                    totalQuestionsInDomain = totalInDomain,
                    readinessScore = readiness
                )
            }
        }
    }
}
