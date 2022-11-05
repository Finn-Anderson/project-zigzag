package com.uhi.mad.zigzag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.uhi.mad.zigzag.databinding.GameOverBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Game_Over : Fragment() {

    private var _binding: GameOverBinding? = null

    val scoreModel: ScoreViewModel by activityViewModels()
    val nameModel: UsernameViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameOverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val score = scoreModel.getScore()
        binding.scoreTxt.text = score

        GameOverController.storeDeviceHighscore(score, requireContext().filesDir)

        binding.submitBtn.setOnClickListener {
            nameModel.setName(binding.usernameInput.text.toString())
            findNavController().navigate(R.id.action_Over_to_Leaderboard)
        }

        binding.playAgainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_Over_to_Game)
        }

        binding.leaderboardButton.setOnClickListener {
            findNavController().navigate(R.id.action_Over_to_Leaderboard)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}