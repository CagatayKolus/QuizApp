package com.cagataykolus.quizapp.data.remote.api

import com.cagataykolus.quizapp.model.Quiz
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
/**
 * Service to fetch data using endpoint [QUIZ_API_URL].
 */
interface QuizApiService {
    @GET("b534c346-b389-41d9-bf25-4123a08d171c")
    suspend fun getQuiz1(): Response<Quiz>

    @GET("b1d8dfc4-3d4c-4ed4-aaf5-adf8c3fb2b65")
    suspend fun getQuiz2(): Response<Quiz>

    @GET("e19ac854-d2aa-491e-9972-7acd62316a0a")
    suspend fun getQuiz3(): Response<Quiz>

    @GET("b1a34bad-e2c2-429e-b4b1-bd6972d9e540")
    suspend fun getQuiz4(): Response<Quiz>

    @GET("3c794462-2d17-497c-b752-c0b3f2703bce")
    suspend fun getQuiz5(): Response<Quiz>

    companion object {
        const val QUIZ_API_URL = "https://run.mocky.io/v3/"
    }
}