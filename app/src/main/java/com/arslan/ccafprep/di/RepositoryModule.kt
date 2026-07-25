package com.arslan.ccafprep.di

import com.arslan.ccafprep.data.repository.FlashcardRepositoryImpl
import com.arslan.ccafprep.data.repository.QuestionRepositoryImpl
import com.arslan.ccafprep.domain.repository.FlashcardRepository
import com.arslan.ccafprep.domain.repository.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuestionRepository(
        questionRepositoryImpl: QuestionRepositoryImpl
    ): QuestionRepository

    @Binds
    @Singleton
    abstract fun bindFlashcardRepository(
        flashcardRepositoryImpl: FlashcardRepositoryImpl
    ): FlashcardRepository
}
