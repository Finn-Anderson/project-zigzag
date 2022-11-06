package com.uhi.mad.zigzag

import android.app.AlertDialog
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

    private val scoreModel: ScoreViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var scoreDatabase: DatabaseHelper? = null
    private var scores = ArrayList<Array<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameOverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scoreTxt = scoreModel.getScore().toString()
        binding.scoreTxt.text = scoreTxt

        GameOverController.storeDeviceHighscore(scoreTxt, requireContext().filesDir)

        binding.submitBtn.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val score = scoreModel.getScore()

            if (score != null) {
                if (username.isNotEmpty() && score > 0) {

                    scoreDatabase = DatabaseHelper(requireContext())
                    scoreDatabase!!.open()
                    scores = scoreDatabase!!.getUser(username)

                    if (scores.size > 0) {
                        scoreDatabase!!.updateScore(username, score, "United Kingdom")
                    } else {
                        scoreDatabase!!.insertScore(username, score, "United Kingdom")
                    }

                    findNavController().navigate(R.id.action_Over_to_Leaderboard)
                } else {
                    alert("Invalid username entered")
                }
            } else {
                alert("Score is too low to submit")
            }
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

    fun alert(message: String){
        val alert = AlertDialog.Builder(requireContext())

        alert.setTitle("Error")
        alert.setMessage(message)
        alert.setCancelable(false)
        alert.setNegativeButton("Ok") { dialog, _ ->
            dialog.cancel()
        }
        alert.create()
        alert.show()
    }
}