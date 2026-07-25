package com.arslan.ccafprep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arslan.ccafprep.data.local.dao.DailyActivityDao
import com.arslan.ccafprep.data.local.dao.FlashcardDao
import com.arslan.ccafprep.data.local.dao.ProgressDao
import com.arslan.ccafprep.data.local.dao.QuestionDao
import com.arslan.ccafprep.data.local.entity.CaseStudyEntity
import com.arslan.ccafprep.data.local.entity.DailyActivityEntity
import com.arslan.ccafprep.data.local.entity.FlashcardEntity
import com.arslan.ccafprep.data.local.entity.ProgressEntity
import com.arslan.ccafprep.data.local.entity.QuestionEntity

@Database(
    entities = [
        QuestionEntity::class, 
        ProgressEntity::class, 
        CaseStudyEntity::class, 
        FlashcardEntity::class,
        DailyActivityEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class CcafDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun progressDao(): ProgressDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun dailyActivityDao(): DailyActivityDao
}
