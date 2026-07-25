package com.arslan.ccafprep.domain.usecase

import com.arslan.ccafprep.data.local.dao.DailyActivityDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class UserStats(
    val streak: Int,
    val weeklyCount: Int,
    val weeklyGoalProgress: Float
)

class GetUserStatsUseCase @Inject constructor(
    private val activityDao: DailyActivityDao
) {
    operator fun invoke(): Flow<UserStats> {
        return activityDao.getLast30DaysActivity().map { activities ->
            val activityMap = activities.associate { it.date to it.count }
            
            // Calculate Streak
            var currentStreak = 0
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            
            // Start checking from today
            var dateToCheck = sdf.format(calendar.time)
            
            // If didn't study today, check if streak still alive from yesterday
            if (!activityMap.containsKey(dateToCheck)) {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                dateToCheck = sdf.format(calendar.time)
            }

            while (activityMap.containsKey(dateToCheck)) {
                currentStreak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                dateToCheck = sdf.format(calendar.time)
            }

            // Calculate Weekly Count (Last 7 Days)
            val weekCalendar = Calendar.getInstance()
            var weeklyTotal = 0
            for (i in 0 until 7) {
                val dayStr = sdf.format(weekCalendar.time)
                weeklyTotal += activityMap[dayStr] ?: 0
                weekCalendar.add(Calendar.DAY_OF_YEAR, -1)
            }

            val goal = 50f // Default goal
            UserStats(
                streak = currentStreak,
                weeklyCount = weeklyTotal,
                weeklyGoalProgress = (weeklyTotal / goal).coerceIn(0f, 1f)
            )
        }
    }
}
