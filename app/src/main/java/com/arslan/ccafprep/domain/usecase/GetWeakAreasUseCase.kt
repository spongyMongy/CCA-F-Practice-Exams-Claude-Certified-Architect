package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.data.local.dao.ProgressDao
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class WeakArea(
    val domain: ExamDomain,
    val accuracy: Float,
    val recommendation: String
)

class GetWeakAreasUseCase @Inject constructor(
    private val repository: QuestionRepository,
    private val progressDao: ProgressDao
) {
    operator fun invoke(): Flow<List<WeakArea>> {
        return combine(
            repository.getAllQuestions(),
            progressDao.getAllProgress()
        ) { allQuestions, allProgress ->
            val progressMap = allProgress.associateBy { it.questionId }
            
            ExamDomain.values().mapNotNull { domain ->
                val domainQuestions = allQuestions.filter { it.domain == domain }
                val domainAttempts = domainQuestions.mapNotNull { progressMap[it.id] }
                
                if (domainAttempts.isEmpty()) return@mapNotNull null
                
                val totalCorrect = domainAttempts.sumOf { it.totalCorrect }
                val totalTries = domainAttempts.sumOf { it.totalAttempts }
                val accuracy = if (totalTries > 0) totalCorrect.toFloat() / totalTries else 0f
                
                if (accuracy < 0.7f) {
                    WeakArea(
                        domain = domain,
                        accuracy = accuracy,
                        recommendation = when (domain) {
                            ExamDomain.AGENTIC_ARCHITECTURE -> "Focus on multi-agent patterns and stop_reason handling."
                            ExamDomain.TOOL_DESIGN -> "Review MCP transport tradeoffs and SSE implementation."
                            ExamDomain.CLAUDE_CODE -> "Deep dive into CLAUDE.md conventions and custom hooks."
                            ExamDomain.PROMPT_ENGINEERING -> "Study JSON schema enforcement and few-shot prompting."
                            ExamDomain.CONTEXT_MANAGEMENT -> "Review prompt caching and RAG pipeline optimization."
                        }
                    )
                } else null
            }.sortedBy { it.accuracy }
        }
    }
}
