package com.arslan.ccafprep.data.local.seed

import android.content.Context
import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.data.local.entity.FlashcardEntity
import com.arslan.ccafprep.domain.model.CaseStudy
import com.arslan.ccafprep.domain.model.Flashcard
import com.arslan.ccafprep.domain.model.Question
import com.arslan.ccafprep.domain.repository.FlashcardRepository
import com.arslan.ccafprep.domain.repository.QuestionRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: QuestionRepository,
    private val flashcardRepository: FlashcardRepository,
    private val settingsManager: SettingsManager
) {
    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val currentIsSeeded = settingsManager.isSeeded.first()
        // Force re-seed if we have very few questions or flashcards (likely an old dev build)
        val questionsCount = repository.getAllQuestions().first().size
        val flashcardsCount = flashcardRepository.getAllFlashcards().first().size
        
        if (!currentIsSeeded || questionsCount < 200 || flashcardsCount < 125) {
            val questions = loadQuestionsFromAssets()
            repository.insertQuestions(questions)
            
            val caseStudies = loadCaseStudiesFromAssets()
            repository.insertCaseStudies(caseStudies)

            val flashcards = loadFlashcardsFromAssets()
            flashcardRepository.insertFlashcards(flashcards)
            
            settingsManager.setSeeded(true)
        }
    }

    private fun loadFlashcardsFromAssets(): List<Flashcard> {
        val json = context.assets.open("flashcards_v1.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<FlashcardDto>>() {}.type
        val dtos: List<FlashcardDto> = Gson().fromJson(json, type)
        return dtos.map { it.toDomain() }
    }

    private fun loadQuestionsFromAssets(): List<Question> {
        val json = context.assets.open("questions_v2.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<QuestionDto>>() {}.type
        val dtos: List<QuestionDto> = Gson().fromJson(json, type)
        return dtos.map { it.toDomain() }
    }

    private fun loadCaseStudiesFromAssets(): List<CaseStudy> {
        val json = context.assets.open("case_studies_v1.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<CaseStudyDto>>() {}.type
        val dtos: List<CaseStudyDto> = Gson().fromJson(json, type)
        return dtos.map { it.toDomain() }
    }
}

data class QuestionDto(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val domainId: Int,
    val difficulty: String,
    val distractorRationale: List<String>? = null,
    val scenarioId: String? = null
) {
    fun toDomain() = com.arslan.ccafprep.domain.model.Question(
        id = id,
        text = text,
        options = options,
        correctIndex = correctIndex,
        explanation = explanation,
        domain = com.arslan.ccafprep.domain.model.ExamDomain.entries.find { it.id == domainId } 
            ?: com.arslan.ccafprep.domain.model.ExamDomain.AGENTIC_ARCHITECTURE,
        difficulty = com.arslan.ccafprep.domain.model.Difficulty.valueOf(difficulty),
        distractorRationale = distractorRationale,
        scenarioId = scenarioId
    )
}

data class CaseStudyDto(
    val id: String,
    val title: String,
    val context: String,
    val domainId: Int
) {
    fun toDomain() = CaseStudy(
        id = id,
        title = title,
        context = context,
        domain = com.arslan.ccafprep.domain.model.ExamDomain.entries.find { it.id == domainId }
            ?: com.arslan.ccafprep.domain.model.ExamDomain.AGENTIC_ARCHITECTURE
    )
}

data class FlashcardDto(
    val id: String,
    val term: String,
    val definition: String,
    val domainId: Int
) {
    fun toDomain() = Flashcard(
        id = id,
        term = term,
        definition = definition,
        domain = com.arslan.ccafprep.domain.model.ExamDomain.entries.find { it.id == domainId }
            ?: com.arslan.ccafprep.domain.model.ExamDomain.AGENTIC_ARCHITECTURE
    )
}
