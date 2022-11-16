package com.uhi.mad.zigzag

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.uhi.mad.zigzag.databinding.GameOverBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Game_Over : Fragment() {

    // Initialise variables
    private var _binding: GameOverBinding? = null
    private val binding get() = _binding!!

    private val scoreModel: ScoreViewModel by activityViewModels()

    private var scoreDatabase: DatabaseHelper? = null
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

        // Sets textview to score
        val scoreTxt = scoreModel.getScore().toString()
        binding.scoreTxt.text = scoreTxt

        // Store new value and send notification if greater than value in file
        GameOverController.storeDeviceHighscore(scoreTxt, activity, requireContext().filesDir)

        // Checks if username and score is set before submitting score
        binding.submitBtn.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val score = scoreModel.getScore()

            if (score != null) {
                if (username.isNotEmpty()) {

                    scoreDatabase = DatabaseHelper(requireContext())
                    scoreDatabase!!.open()
                    scores = scoreDatabase!!.getUser(username)

                    // Get user's location before continuing
                    LocationGrabber().getLocation(requireContext(), requireActivity()) { country ->

                        // Check if username already exists
                        if (scores.size > 0) {
                            // Checks if the username has a greater score than the one just set
                            if (score > scores[0][1].toInt()) {
                                scoreDatabase!!.updateScore(username, score, country)

                                findNavController().navigate(R.id.action_Over_to_Leaderboard)
                            } else {
                                alert("Someone has entered a higher score with this username")
                            }
                        } else {
                            scoreDatabase!!.insertScore(username, score, country)

                            findNavController().navigate(R.id.action_Over_to_Leaderboard)
                        }
                    }
                } else {
                    alert("Invalid username entered")
                }
            } else {
                alert("Score is too low to submit")
            }
        }

        // Navigation buttons to game and leaderboard respectively
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

    // Displays error message when the user submits a low score/invalid username
    private fun alert(message: String){
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