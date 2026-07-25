package com.arslan.ccafprep.domain.model

data class Flashcard(
    val id: String,
    val term: String,
    val definition: String,
    val domain: ExamDomain
)
