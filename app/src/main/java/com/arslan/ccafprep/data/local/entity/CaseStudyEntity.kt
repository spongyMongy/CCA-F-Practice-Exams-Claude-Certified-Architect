package com.arslan.ccafprep.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arslan.ccafprep.domain.model.CaseStudy
import com.arslan.ccafprep.domain.model.ExamDomain

@Entity(tableName = "case_studies")
data class CaseStudyEntity(
    @PrimaryKey val id: String,
    val title: String,
    val context: String,
    val domainId: Int
) {
    fun toDomain() = CaseStudy(
        id = id,
        title = title,
        context = context,
        domain = ExamDomain.entries.find { it.id == domainId } ?: ExamDomain.AGENTIC_ARCHITECTURE
    )
    
    companion object {
        fun fromDomain(cs: CaseStudy) = CaseStudyEntity(
            id = cs.id,
            title = cs.title,
            context = cs.context,
            domainId = cs.domain.id
        )
    }
}
