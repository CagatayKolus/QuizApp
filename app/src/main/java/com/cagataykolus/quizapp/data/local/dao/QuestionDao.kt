package com.cagataykolus.quizapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cagataykolus.quizapp.model.Question
import kotlinx.coroutines.flow.Flow

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
/**
 * Data Access Object (DAO) for [com.cagataykolus.quizapp.data.local.QuestionDatabase]
 */
@Dao
interface QuestionDao {
    /**
     * Inserts [questions] into the [Question.TABLE_QUESTION] table.
     * Duplicate values are replaced in the table.
     * @param questions
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuestions(questions: List<Question>)

    /**
     * Fetches all the data from the [Question.TABLE_QUESTION] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${Question.TABLE_QUESTION}")
    fun getAllQuestions(): Flow<List<Question>>

    /**
     * Deletes all the data from the [Question.TABLE_QUESTION] table.
     */
    @Query("DELETE FROM ${Question.TABLE_QUESTION}")
    suspend fun deleteAllQuestions()
}