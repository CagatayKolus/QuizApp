package com.cagataykolus.quizapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cagataykolus.quizapp.model.Question.Companion.TABLE_QUESTION
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Quiz(
    val questionList: List<Question>
) : Parcelable {
}

@Parcelize
@Entity(tableName = TABLE_QUESTION)
@TypeConverters(
    QuestionConverter::class,
    AnswerConverter::class
)
data class Question(
    val answer: @RawValue Answer,
    @PrimaryKey
    val questionId: Int,
    val questionText: String,
) : Parcelable {
    companion object {
        const val TABLE_QUESTION = "table_question"
    }
}

data class Answer(
    var option: List<Option>,
    var correctOptionId: Int
)

data class Option(
    var id: Int,
    var text: String
)