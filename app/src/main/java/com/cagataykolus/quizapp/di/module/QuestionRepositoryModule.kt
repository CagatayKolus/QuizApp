package com.cagataykolus.quizapp.di.module

import com.cagataykolus.quizapp.data.repository.DefaultQuestionRepository
import com.cagataykolus.quizapp.data.repository.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class QuestionRepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindQuestionRepository(repository: DefaultQuestionRepository): QuestionRepository
}