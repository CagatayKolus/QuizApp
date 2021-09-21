package com.cagataykolus.quizapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cagataykolus.quizapp.data.local.dao.QuestionDao
import com.cagataykolus.quizapp.model.Question

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
/**
 * QuestionDatabase provides DAO [QuestionDao] by using method [getQuestionDao].
 */
@Database(
    entities = [Question::class],
    version = MigrationDatabase.DB_VERSION
)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun getQuestionDao(): QuestionDao

    companion object {
        private const val DB_NAME = "database_quiz"

        @Volatile
        private var INSTANCE: QuestionDatabase? = null

        fun getInstance(context: Context): QuestionDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionDatabase::class.java,
                    DB_NAME
                ).addMigrations(*MigrationDatabase.MIGRATION_HIT).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
