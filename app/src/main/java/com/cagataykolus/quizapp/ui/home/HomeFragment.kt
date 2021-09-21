package com.cagataykolus.quizapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cagataykolus.quizapp.R
import com.cagataykolus.quizapp.databinding.FragmentHomeBinding
import com.cagataykolus.quizapp.util.viewBinding

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val round = arguments?.getInt("NEXT_ROUND")
        if (round != null) {
            // Show in the Round 2,3,4,5
            showRoundUI(round)
            binding.buttonHomeStart.setOnClickListener {
                navigateToQuizFragment(round)
            }
            if (round == 6) { // Restart rounds
                showWelcomeUI()
                binding.buttonHomeStart.setOnClickListener {
                    navigateToQuizFragment(1)
                }
            }
        } else {
            // Shown in the Round 1
            showWelcomeUI()
            binding.buttonHomeStart.setOnClickListener {
                navigateToQuizFragment(1)
            }
        }
    }

    private fun showWelcomeUI() {
        binding.textviewHomeTitle.text = getString(R.string.home_title)
        binding.textviewHomeDescription.text = getString(R.string.home_description)
        binding.textviewHomeRound.text = getString(R.string.home_round).plus(" ").plus("1")
    }

    private fun showRoundUI(round: Int) {
        binding.textviewHomeTitle.text = getString(R.string.home_title_2)
        binding.textviewHomeDescription.text = getString(R.string.home_description_2)
        binding.textviewHomeRound.text =
            getString(R.string.home_round).plus(" ").plus(round.toString())
    }

    private fun navigateToQuizFragment(round: Int) {
        val bundle = bundleOf("CURRENT_ROUND" to round)
        findNavController().navigate(R.id.action_homeFragment_to_quizFragment, bundle)
    }
}