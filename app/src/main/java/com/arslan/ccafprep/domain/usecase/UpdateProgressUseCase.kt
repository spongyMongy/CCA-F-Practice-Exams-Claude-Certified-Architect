package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.data.local.dao.DailyActivityDao
import com.arslan.ccafprep.data.local.dao.ProgressDao
import com.arslan.ccafprep.data.local.entity.DailyActivityEntity
import com.arslan.ccafprep.data.local.entity.ProgressEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UpdateProgressUseCase @Inject constructor(
    private val progressDao: ProgressDao,
    private val activityDao: DailyActivityDao,
    private val settingsManager: SettingsManager
) {
    suspend operator fun invoke(questionId: String, isCorrect: Boolean) {
        val currentProgress = progressDao.getProgressForQuestion(questionId) 
            ?: ProgressEntity(questionId = questionId)

        val quality = if (isCorrect) 5 else 0
        val updatedProgress = calculateSM2(currentProgress, quality)
        
        progressDao.updateProgress(updatedProgress)
        
        // Track daily activity volume
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val rowsAffected = activityDao.incrementCount(today)
        if (rowsAffected == 0) {
            activityDao.insertActivity(DailyActivityEntity(today, 1))
        }
    }

    private fun calculateSM2(progress: ProgressEntity, quality: Int): ProgressEntity {
        var interval: Int
        var nextEasinessFactor: Float
        var streak: Int

        if (quality >= 3) {
            streak = progress.correctStreak + 1
            interval = when (streak) {
                1 -> 1
                2 -> 6
                else -> (progress.interval * progress.easinessFactor).toInt()
            }
            nextEasinessFactor = progress.easinessFactor + (0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f))
        } else {
            streak = 0
            interval = 1
            nextEasinessFactor = progress.easinessFactor
        }

        if (nextEasinessFactor < 1.3f) nextEasinessFactor = 1.3f

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, interval)
        val nextReview = calendar.timeInMillis

        return progress.copy(
            correctStreak = streak,
            lastAttemptTime = System.currentTimeMillis(),
            nextReviewTime = nextReview,
            totalAttempts = progress.totalAttempts + 1,
            totalCorrect = if (quality >= 3) progress.totalCorrect + 1 else progress.totalCorrect,
            easinessFactor = nextEasinessFactor,
            interval = interval
        )
    }
}
