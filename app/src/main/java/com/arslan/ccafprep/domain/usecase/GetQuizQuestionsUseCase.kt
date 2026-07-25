package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Question
import com.arslan.ccafprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetQuizQuestionsUseCase @Inject constructor(
    private val repository: QuestionRepository,
    private val settingsManager: SettingsManager
) {
    operator fun invoke(mode: String, domainId: Int): Flow<List<Question>> {
        return combine(
            repository.getAllQuestions(),
            settingsManager.isProUnlocked
        ) { allQuestions, isPro ->
            val availableQuestions = if (isPro) {
                allQuestions
            } else {
                // Free tier only has Domain 1
                allQuestions.filter { it.domain.id == 1 }
            }

            when {
                mode == "domain" && domainId != -1 -> {
                    val domain = ExamDomain.values().firstOrNull { it.id == domainId }
                    if (domain != null && (isPro || domain.id == 1)) {
                        availableQuestions.filter { it.domain == domain }.shuffled()
                    } else {
                        emptyList()
                    }
                }
                mode == "mock" -> {
                    if (isPro) availableQuestions.shuffled().take(60) else emptyList()
                }
                else -> availableQuestions.shuffled()
            }
        }
    }
}
