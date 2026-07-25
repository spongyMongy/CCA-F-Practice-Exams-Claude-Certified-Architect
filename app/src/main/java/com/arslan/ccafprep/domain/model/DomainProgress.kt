package com.arslan.ccafprep.domain.model

data class DomainProgress(
    val domain: ExamDomain,
    val accuracy: Float, // 0.0 to 1.0
    val questionsAttempted: Int,
    val totalQuestionsInDomain: Int,
    val readinessScore: Float // 0.0 to 1.0
)
