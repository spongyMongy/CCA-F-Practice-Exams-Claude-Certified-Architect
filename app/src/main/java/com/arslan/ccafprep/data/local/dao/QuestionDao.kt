package com.arslan.ccafprep.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arslan.ccafprep.data.local.entity.CaseStudyEntity
import com.arslan.ccafprep.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE domainId = :domainId")
    fun getQuestionsByDomain(domainId: Int): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("SELECT * FROM case_studies")
    fun getAllCaseStudies(): Flow<List<CaseStudyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaseStudies(caseStudies: List<CaseStudyEntity>)
}
