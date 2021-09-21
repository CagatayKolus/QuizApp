package com.cagataykolus.quizapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cagataykolus.quizapp.model.Question

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
/**
 * Each Migration has a start and end versions and Room runs these migrations to bring the
 * database to the latest version. The migration object that can modify the database and
 * to the necessary changes.
 */
object MigrationDatabase {
    const val DB_VERSION = 2

    val MIGRATION_HIT: Array<Migration>
        get() = arrayOf(
            migrationQuestion()
        )

    /**
     *  Creates a new migration between version 1 and 2 for [Question.TABLE_QUESTION] table.
     */
    private fun migrationQuestion(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${Question.TABLE_QUESTION} ADD COLUMN body TEXT")
        }
    }
}
