package com.arslan.ccafprep.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Flashcard

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val term: String,
    val definition: String,
    val domainId: Int
) {
    fun toDomain(): Flashcard {
        return Flashcard(
            id = id,
            term = term,
            definition = definition,
            domain = ExamDomain.entries.find { it.id == domainId } ?: ExamDomain.AGENTIC_ARCHITECTURE
        )
    }

    companion object {
        fun fromDomain(f: Flashcard): FlashcardEntity {
            return FlashcardEntity(
                id = f.id,
                term = f.term,
                definition = f.definition,
                domainId = f.domain.id
            )
        }
    }
}
