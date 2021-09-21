package com.cagataykolus.quizapp.di.module

import android.app.Application
import com.cagataykolus.quizapp.data.local.QuestionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
@InstallIn(SingletonComponent::class)
@Module
class QuestionDatabaseModule {
    @Singleton
    @Provides
    fun provideQuestionDatabase(application: Application) = QuestionDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideQuestionDao(database: QuestionDatabase) = database.getQuestionDao()
}