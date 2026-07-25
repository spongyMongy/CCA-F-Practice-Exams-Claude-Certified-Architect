package com.arslan.ccafprep.data.repository

import com.arslan.ccafprep.data.local.dao.QuestionDao
import com.arslan.ccafprep.data.local.entity.CaseStudyEntity
import com.arslan.ccafprep.data.local.entity.QuestionEntity
import com.arslan.ccafprep.domain.model.CaseStudy
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.domain.model.Question
import com.arslan.ccafprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {

    override fun getAllQuestions(): Flow<List<Question>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getQuestionsByDomain(domain: ExamDomain): Flow<List<Question>> {
        return questionDao.getQuestionsByDomain(domain.id).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertQuestions(questions: List<Question>) {
        questionDao.insertQuestions(questions.map { QuestionEntity.fromDomain(it) })
    }

    override fun getAllCaseStudies(): Flow<List<CaseStudy>> {
        return questionDao.getAllCaseStudies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertCaseStudies(caseStudies: List<CaseStudy>) {
        questionDao.insertCaseStudies(caseStudies.map { CaseStudyEntity.fromDomain(it) })
    }
}
