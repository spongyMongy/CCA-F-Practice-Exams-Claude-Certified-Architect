package com.arslan.ccafprep.domain.repository

import com.arslan.ccafprep.domain.model.CaseStudy
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    fun getAllQuestions(): Flow<List<Question>>
    fun getQuestionsByDomain(domain: ExamDomain): Flow<List<Question>>
    suspend fun insertQuestions(questions: List<Question>)
    
    fun getAllCaseStudies(): Flow<List<CaseStudy>>
    suspend fun insertCaseStudies(caseStudies: List<CaseStudy>)
}
