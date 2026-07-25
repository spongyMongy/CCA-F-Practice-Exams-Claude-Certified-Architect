package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Flashcard
import com.arslan.ccafprep.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFlashcardsUseCase @Inject constructor(
    private val repository: FlashcardRepository
) {
    operator fun invoke(domain: ExamDomain? = null): Flow<List<Flashcard>> {
        return if (domain != null) {
            repository.getFlashcardsByDomain(domain)
        } else {
            repository.getAllFlashcards()
        }
    }
}
