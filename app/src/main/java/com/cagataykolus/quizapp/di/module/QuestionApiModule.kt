package com.cagataykolus.quizapp.di.module

import com.cagataykolus.quizapp.data.remote.api.QuizApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
@InstallIn(SingletonComponent::class)
@Module
class QuestionApiModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): QuizApiService = Retrofit.Builder()
        .baseUrl(QuizApiService.QUIZ_API_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .build()
        .create(QuizApiService::class.java)
}
