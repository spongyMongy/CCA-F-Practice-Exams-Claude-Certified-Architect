package com.arslan.ccafprep.domain.model

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val domain: ExamDomain,
    val difficulty: Difficulty,
    val distractorRationale: List<String>? = null, // One per option, explaining why it's wrong/distractor
    val scenarioId: String? = null // Links questions to a shared Case Study
)

data class CaseStudy(
    val id: String,
    val title: String,
    val context: String,
    val domain: ExamDomain
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}
