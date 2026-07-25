package com.arslan.ccafprep.di

import android.content.Context
import androidx.room.Room
import com.arslan.ccafprep.data.local.CcafDatabase
import com.arslan.ccafprep.data.local.dao.DailyActivityDao
import com.arslan.ccafprep.data.local.dao.FlashcardDao
import com.arslan.ccafprep.data.local.dao.ProgressDao
import com.arslan.ccafprep.data.local.dao.QuestionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CcafDatabase {
        return Room.databaseBuilder(
            context,
            CcafDatabase::class.java,
            "ccaf_database"
        )
        .fallbackToDestructiveMigration() // FIX: Rebuild DB on schema change to prevent crash
        .build()
    }

    @Provides
    fun provideQuestionDao(db: CcafDatabase): QuestionDao = db.questionDao()

    @Provides
    fun provideProgressDao(db: CcafDatabase): ProgressDao = db.progressDao()

    @Provides
    fun provideFlashcardDao(db: CcafDatabase): FlashcardDao = db.flashcardDao()

    @Provides
    fun provideDailyActivityDao(db: CcafDatabase): DailyActivityDao = db.dailyActivityDao()
}
