package com.cagataykolus.quizapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResult(
    val round: Int,
    val successfulAnswerCount: Int,
    val failedAnswerCount: Int,
    val lifeTime: Int,
    val quizFinishType: QuizFinishType
) : Parcelable