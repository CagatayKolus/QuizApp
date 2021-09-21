package com.cagataykolus.quizapp.ui.summary

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cagataykolus.quizapp.R
import com.cagataykolus.quizapp.databinding.FragmentSummaryBinding
import com.cagataykolus.quizapp.model.QuizFinishType
import com.cagataykolus.quizapp.model.QuizResult
import com.cagataykolus.quizapp.util.viewBinding

/**
 * Created by Çağatay Kölüş on 20.09.2021.
 * cagataykolus@gmail.com
 */
class SummaryFragment : Fragment(R.layout.fragment_summary) {
    private val binding by viewBinding { FragmentSummaryBinding.bind(it) }
    private val viewModel by viewModels<SummaryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getParcelable<QuizResult>("RESULT")
        content?.let {
            binding.textviewSummarySuccessful.text = it.successfulAnswerCount.toString()
            binding.textviewSummaryFailed.text = it.failedAnswerCount.toString()
            binding.textviewSummaryLifetime.text = binding.textviewSummaryLifetime.text.toString().plus(": ").plus(it.lifeTime.toString())

                when (it.quizFinishType) {
                QuizFinishType.FINISHED -> {
                    binding.textviewSummaryTitle.text = getString(R.string.summary_finished)
                    binding.textviewSummaryDescription.text = getString(R.string.summary_finished_description)
                    binding.buttonSummaryContinue.text = getString(R.string.summary_positive_button)
                }
                QuizFinishType.TIMEOUT -> {
                    binding.textviewSummaryTitle.text = getString(R.string.summary_timeout)
                    binding.textviewSummaryDescription.text = getString(R.string.summary_timeout_description)
                    binding.buttonSummaryContinue.text = getString(R.string.summary_negative_button)
                }
            }
        }

        binding.buttonSummaryContinue.setOnClickListener {
            val bundle = bundleOf("NEXT_ROUND" to content?.round?.plus(1))
            when (content?.quizFinishType) {
                QuizFinishType.FINISHED -> {
                    findNavController().navigate(R.id.action_summaryFragment_to_homeFragment, bundle)
                }
                QuizFinishType.TIMEOUT -> {
                    findNavController().navigate(R.id.action_summaryFragment_to_homeFragment)
                }
            }
        }
    }
}