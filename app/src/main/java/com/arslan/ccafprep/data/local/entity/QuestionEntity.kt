package com.arslan.ccafprep.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arslan.ccafprep.domain.model.Difficulty
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val text: String,
    val optionsJson: String,
    val correctIndex: Int,
    val explanation: String,
    val domainId: Int,
    val difficulty: String,
    val distractorRationaleJson: String? = null,
    val scenarioId: String? = null
) {
    fun toDomain(): Question {
        val type = object : TypeToken<List<String>>() {}.type
        return Question(
            id = id,
            text = text,
            options = Gson().fromJson(optionsJson, type),
            correctIndex = correctIndex,
            explanation = explanation,
            domain = ExamDomain.entries.find { it.id == domainId } ?: ExamDomain.AGENTIC_ARCHITECTURE,
            difficulty = Difficulty.valueOf(difficulty),
            distractorRationale = distractorRationaleJson?.let { Gson().fromJson(it, type) },
            scenarioId = scenarioId
        )
    }

    companion object {
        fun fromDomain(q: Question): QuestionEntity {
            return QuestionEntity(
                id = q.id,
                text = q.text,
                optionsJson = Gson().toJson(q.options),
                correctIndex = q.correctIndex,
                explanation = q.explanation,
                domainId = q.domain.id,
                difficulty = q.difficulty.name,
                distractorRationaleJson = q.distractorRationale?.let { Gson().toJson(it) },
                scenarioId = q.scenarioId
            )
        }
    }
}
