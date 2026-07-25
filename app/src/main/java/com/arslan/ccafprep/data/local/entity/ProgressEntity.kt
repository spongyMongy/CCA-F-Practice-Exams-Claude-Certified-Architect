package com.arslan.ccafprep.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class ProgressEntity(
    @PrimaryKey val questionId: String,
    val correctStreak: Int = 0,
    val lastAttemptTime: Long = 0,
    val nextReviewTime: Long = 0,
    val totalAttempts: Int = 0,
    val totalCorrect: Int = 0,
    val easinessFactor: Float = 2.5f,
    val interval: Int = 0
)
