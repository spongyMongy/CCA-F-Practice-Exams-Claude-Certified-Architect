package com.arslan.ccafprep.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arslan.ccafprep.data.local.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE questionId = :questionId")
    suspend fun getProgressForQuestion(questionId: String): ProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: ProgressEntity)
}
