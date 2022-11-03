package com.uhi.mad.zigzag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.uhi.mad.zigzag.databinding.LeaderboardBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Leaderboard : Fragment() {

    private var _binding: LeaderboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playButton.setOnClickListener {
            findNavController().navigate(R.id.action_Leaderboard_to_Game)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}