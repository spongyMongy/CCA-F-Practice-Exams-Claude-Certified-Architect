package com.arslan.ccafprep.data.repository

import com.arslan.ccafprep.data.local.dao.FlashcardDao
import com.arslan.ccafprep.data.local.entity.FlashcardEntity
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Flashcard
import com.arslan.ccafprep.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashcardRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {

    override fun getAllFlashcards(): Flow<List<Flashcard>> {
        return flashcardDao.getAllFlashcards().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFlashcardsByDomain(domain: ExamDomain): Flow<List<Flashcard>> {
        return flashcardDao.getFlashcardsByDomain(domain.id).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertFlashcards(flashcards: List<Flashcard>) {
        flashcardDao.insertFlashcards(flashcards.map { FlashcardEntity.fromDomain(it) })
    }
}
