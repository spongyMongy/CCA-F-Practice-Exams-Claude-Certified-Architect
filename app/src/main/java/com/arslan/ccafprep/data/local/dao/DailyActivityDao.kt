package com.arslan.ccafprep.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arslan.ccafprep.data.local.entity.DailyActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyActivityDao {
    @Query("SELECT * FROM daily_activity WHERE date = :date")
    suspend fun getActivityForDate(date: String): DailyActivityEntity?

    @Query("SELECT * FROM daily_activity ORDER BY date DESC LIMIT 30")
    fun getLast30DaysActivity(): Flow<List<DailyActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: DailyActivityEntity)
    
    @Query("UPDATE daily_activity SET count = count + 1 WHERE date = :date")
    suspend fun incrementCount(date: String): Int
}
