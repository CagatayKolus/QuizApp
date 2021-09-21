package com.cagataykolus.quizapp.ui.quiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataykolus.quizapp.data.repository.QuestionRepository
import com.cagataykolus.quizapp.model.Question
import com.cagataykolus.quizapp.model.State
import com.cagataykolus.quizapp.util.CountDownTimerExt
import com.cagataykolus.quizapp.util.QuizSettings
import com.cagataykolus.quizapp.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class QuizViewModel @Inject constructor(private val questionRepository: QuestionRepository) :
    ViewModel() {

    /**
     * Getting Questions
     * */
    private val _questionsLiveData = MutableLiveData<State<List<Question>>>()
    val questionsLiveData: LiveData<State<List<Question>>> = _questionsLiveData

    fun getQuestions(round: Int) {
        viewModelScope.launch {
            questionRepository.deleteAllQuestions()
                .onStart {}
                .map {}
                .collect {}
        }

        val timer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                viewModelScope.launch {
                    questionRepository.getAllQuestions(round)
                        .onStart { _questionsLiveData.value = State.loading() }
                        .map { resource -> State.fromResource(resource) }
                        .collect { state -> _questionsLiveData.value = state }
                }
            }
        }
        timer.start()
    }

    /**
     * Remaining Time
     * */
    private val remainingTimeLiveDataPrivate = MutableLiveData<SingleEvent<Int>>()
    val remainingTimeLiveData: LiveData<SingleEvent<Int>> get() = remainingTimeLiveDataPrivate
    private var remainingTimeTimer: CountDownTimer? = null

    fun startTimer() {
        remainingTimeTimer = object : CountDownTimer(QuizSettings.remainingTimeTimeout.toLong() * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeLiveDataPrivate.value =
                    SingleEvent((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                Log.d("cgtyx", "startTimer onFinish")
                startLifeTimeTimer()
            }
        }
        remainingTimeTimer?.start()
    }

    fun restartTimer() {
        remainingTimeTimer?.cancel()
        startTimer()
        pauseLifeTimeTimer()
    }

    /**
     * Life Time
     * */
    private val lifeTimeLiveDataPrivate = MutableLiveData<SingleEvent<Int>>()
    val lifeTimeLiveData: LiveData<SingleEvent<Int>> get() = lifeTimeLiveDataPrivate
    private var lifeTimeTimer: CountDownTimerExt? = null
    private var isLifeTimeInitialized = false

    fun startLifeTimeTimer() {
        if (isLifeTimeInitialized) {
            resumeLifeTimeTimer()
        } else {
            isLifeTimeInitialized = true
            lifeTimeTimer = object : CountDownTimerExt(QuizSettings.lifeTimeTimeout.toLong() * 1000, 1000) {
                override fun onTimerTick(millisUntilFinished: Long) {
                    lifeTimeLiveDataPrivate.value =
                        SingleEvent((millisUntilFinished / 1000).toInt())
                }

                override fun onTimerFinish() {
                    // Game Over
                }
            }
            lifeTimeTimer?.start()
        }
    }

    fun pauseLifeTimeTimer() {
        lifeTimeTimer?.pause()
    }

    // Resume
    fun resumeLifeTimeTimer() {
        lifeTimeTimer?.start()
    }
}
