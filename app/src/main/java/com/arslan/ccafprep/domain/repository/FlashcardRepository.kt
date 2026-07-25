package com.arslan.ccafprep.domain.repository

import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getAllFlashcards(): Flow<List<Flashcard>>
    fun getFlashcardsByDomain(domain: ExamDomain): Flow<List<Flashcard>>
    suspend fun insertFlashcards(flashcards: List<Flashcard>)
}
