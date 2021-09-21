package com.cagataykolus.quizapp.data.repository

import com.cagataykolus.quizapp.model.Quiz
import com.cagataykolus.quizapp.data.local.dao.QuestionDao
import com.cagataykolus.quizapp.data.remote.api.QuizApiService
import com.cagataykolus.quizapp.model.Question
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
interface QuestionRepository {
    fun getAllQuestions(round: Int
    ): Flow<Resource<List<Question>>>

    fun deleteAllQuestions(
    ): Flow<Resource<List<Question>>>
}

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability. This is single source of data.
 */
@ExperimentalCoroutinesApi
class DefaultQuestionRepository @Inject constructor(
    private val dao: QuestionDao,
    private val service: QuizApiService
) : QuestionRepository {
    /**
     * Fetched the data from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override fun getAllQuestions(round: Int
    ): Flow<Resource<List<Question>>> {
        return object : NetworkBoundRepository<List<Question>, Quiz>() {

            override suspend fun saveRemoteData(response: Quiz) = dao.addQuestions(response.questionList)

            override fun fetchFromLocal(): Flow<List<Question>> = dao.getAllQuestions()

            override suspend fun fetchFromRemote(): Response<Quiz> {
                when(round){
                    1 -> {
                        return service.getQuiz1()
                    }
                    2 -> {
                        return service.getQuiz2()
                    }
                    3 -> {
                        return service.getQuiz3()
                    }
                    4 -> {
                        return service.getQuiz4()
                    }
                    5 -> {
                        return service.getQuiz5()
                    }
                }
                return service.getQuiz1()
            }


        }.asFlow()
    }
    /**
     * Deletes all data.
     */
    override fun deleteAllQuestions(): Flow<Resource<List<Question>>> {
        return object : NetworkBoundRepository<List<Question>, Quiz>() {

            override suspend fun saveRemoteData(response: Quiz) = dao.deleteAllQuestions()

            override fun fetchFromLocal(): Flow<List<Question>> {
                return dao.getAllQuestions()
            }

            override suspend fun fetchFromRemote(): Response<Quiz> {
                return service.getQuiz1()
            }

        }.asFlow()
    }
}