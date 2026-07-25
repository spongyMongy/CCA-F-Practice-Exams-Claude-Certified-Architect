package com.arslan.ccafprep.data.local.seed

data class QuestionJson(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val domainId: Int,
    val difficulty: String
)
