package com.cagataykolus.quizapp.ui.quiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import coil.load
import com.cagataykolus.quizapp.R
import com.cagataykolus.quizapp.databinding.FragmentQuizBinding
import com.cagataykolus.quizapp.model.*
import com.cagataykolus.quizapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val binding by viewBinding { FragmentQuizBinding.bind(it) }
    private val viewModel by viewModels<QuizViewModel>()

    private val hashMap: HashMap<Button, Option> = HashMap(4)

    private var questionList: List<Question> = emptyList()
    private var currentRound = 1
    private var currentCorrectOptionId = 0
    private var currentQuestionNumber = 0
    private var successfulAnswerCount = 0
    private var failedAnswerCount = 0
    private var isLoaded = false

    override fun onStart() {
        super.onStart()

        handleNetworkChanges()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getInt("CURRENT_ROUND")
        content?.let {
            currentRound = content
        }

        getQuestions(currentRound)
        observeQuestions()

        observeEvent(viewModel.remainingTimeLiveData, ::handleRemainingTime)
        observeEvent(viewModel.lifeTimeLiveData, ::handleLifeTime)
    }

    private fun handleRemainingTime(status: SingleEvent<Int>) {
        status.getContentIfNotHandled()?.let {
            binding.textviewQuizRemainingTime.text = it.toString()
            binding.progressbarQuizRemainingTime.progress = it
            if (it <= 0) {
                viewModel.resumeLifeTimeTimer()
            } else {
                viewModel.pauseLifeTimeTimer()
            }
        }
    }

    private fun handleLifeTime(status: SingleEvent<Int>) {
        status.getContentIfNotHandled()?.let {
            binding.textviewQuizLifeTime.text = it.toString()
            binding.progressbarQuizLifeTime.progress = it
            if (it <= 0) { // Game Over
                val quizResult = QuizResult(
                    round = currentRound,
                    successfulAnswerCount = successfulAnswerCount,
                    failedAnswerCount = failedAnswerCount,
                    lifeTime = 0,
                    quizFinishType = QuizFinishType.TIMEOUT
                )
                val bundle = bundleOf("RESULT" to quizResult)
                findNavController().navigate(R.id.action_quizFragment_to_summaryFragment,bundle)
            } else {

            }
        }
    }

    /**
     * Observe data.
     */
    private fun observeQuestions() {
        viewModel.questionsLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    showLoading(true)
                }
                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        if(!isLoaded){
                            Log.d("cgty", "state.data.isNotEmpty()")
                            questionList = state.data.shuffled()
                            startQuiz()
                            showLoading(false)
                            isLoaded = true
                        }
                    }
                }
                is State.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun startQuiz() {
        currentQuestionNumber = 0
        showQuestionAndAnswer()
        viewModel.startTimer()
        binding.textviewQuizRemainingTime.text = QuizSettings.remainingTimeTimeout.toString()
        binding.textviewQuizLifeTime.text = QuizSettings.lifeTimeTimeout.toString()
    }

    private fun showQuestionAndAnswer() {
        val num = currentQuestionNumber
        if (num >= questionList.size) {
            // Round finished
            val quizResult = QuizResult(
                round = currentRound,
                successfulAnswerCount = successfulAnswerCount,
                failedAnswerCount = failedAnswerCount,
                lifeTime = binding.textviewQuizLifeTime.text.toString().toInt(),
                quizFinishType = QuizFinishType.FINISHED
            )
            val bundle = bundleOf("RESULT" to quizResult)
            findNavController().navigate(R.id.action_quizFragment_to_summaryFragment, bundle)
        } else {
            Log.d("cgty", "showQuestionAndAnswer -> $num")
            // Set question text
            // Check for text or image question
            if (questionList[num].questionText.contains(".jpg") ||
                questionList[num].questionText.contains(".png")
            ) {
                // If image
                binding.textviewQuizQuestion.visibility = View.GONE
                binding.textviewQuizImage.visibility = View.VISIBLE
                binding.imageviewQuizImage.visibility = View.VISIBLE
                binding.imageviewQuizImage.load(questionList[num].questionText) // Load image via coil library

            } else {
                // If text
                binding.textviewQuizQuestion.visibility = View.VISIBLE
                binding.textviewQuizImage.visibility = View.GONE
                binding.imageviewQuizImage.visibility = View.GONE
                binding.textviewQuizQuestion.text = questionList[num].questionText
            }

            // Set answers
            val answerList = questionList[num].answer.option.shuffled()
            initAnswer(answerList)
            selectAnswer()

            // Store correct answer id
            currentCorrectOptionId = questionList[num].answer.correctOptionId
        }
    }

    private fun initAnswer(option: List<Option>) {
        hashMap[binding.buttonQuizAnswerA] = option[0]
        hashMap[binding.buttonQuizAnswerB] = option[1]
        hashMap[binding.buttonQuizAnswerC] = option[2]
        hashMap[binding.buttonQuizAnswerD] = option[3]

        for (button in hashMap.keys) {
            button.text = hashMap[button]?.text
        }
    }

    private fun selectAnswer() {
        binding.buttonQuizAnswerA.setOnClickListener {
            decideForOption(binding.buttonQuizAnswerA)
        }
        binding.buttonQuizAnswerB.setOnClickListener {
            decideForOption(binding.buttonQuizAnswerB)
        }
        binding.buttonQuizAnswerC.setOnClickListener {
            decideForOption(binding.buttonQuizAnswerC)
        }
        binding.buttonQuizAnswerD.setOnClickListener {
            decideForOption(binding.buttonQuizAnswerD)
        }
    }

    private fun decideForOption(button: Button) {
        val result = isAnswerCorrect(button)
        Log.d("cgty", "isAnswerCorrect-> $result")
        if (result) {
            // CORRECT ANSWER
            successfulAnswerCount++
            nextQuestion()
        } else {
            // WRONG ANSWER
            failedAnswerCount++
            nextQuestion()
        }
    }

    private fun isAnswerCorrect(button: Button): Boolean {
        for (key in hashMap.keys) {
            if (key == button && hashMap[key]?.id == currentCorrectOptionId) {
                return true
            }
        }
        return false
    }

    private fun nextQuestion() {
        viewModel.restartTimer()
        currentQuestionNumber++
        showQuestionAndAnswer()
    }

    private fun getQuestions(round: Int) = viewModel.getQuestions(round)

    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressbarQuizLoading.visibility = View.VISIBLE
        else {
            binding.progressbarQuizLoading.visibility = View.GONE
        }
    }

    /**
     * Observes network changes.
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(requireContext()).observe(this) { isConnected ->
            if (!isConnected) {
                binding.textviewQuizNetworkStatus.text =
                    getString(R.string.internet_connectivity_fail)
                binding.linearlayoutQuizNetworkStatus.apply {
                    show()
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.connectivity_fail
                        )
                    )
                }
            } else {
//                if (viewModel.questionsLiveData.value is State.Error || mAdapter.itemCount == 0) {
//                    getQuestions(searchView.query.toString())
//                }
                binding.textviewQuizNetworkStatus.text =
                    getString(R.string.internet_connectivity_success)
                binding.linearlayoutQuizNetworkStatus.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.connectivity_success
                        )
                    )

                    animate()
                        .alpha(1f)
                        .setStartDelay(1000L)
                        .setDuration(1000L)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        }
    }
}