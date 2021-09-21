package com.cagataykolus.quizapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuestionConverter {
    companion object {
        private val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun toQuestion(data: String?): Question {
            if (data == null) {
                val answer = Answer(listOf(), 0)
                return Question(answer, 0, "empty")
            }
            val listType = object : TypeToken<Question>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromQuestion(data: Question): String {
            return gson.toJson(data)
        }
    }
}

class AnswerConverter {
    companion object {
        private val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun toAnswer(data: String?): Answer {
            if (data == null) {
                return Answer(listOf(), 0)
            }
            val listType = object : TypeToken<Answer>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromAnswer(data: Answer): String {
            return gson.toJson(data)
        }
    }
}