package com.arslan.ccafprep.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_activity")
data class DailyActivityEntity(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val count: Int = 0
)
