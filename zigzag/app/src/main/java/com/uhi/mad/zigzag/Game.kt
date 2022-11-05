package com.uhi.mad.zigzag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.uhi.mad.zigzag.databinding.GameBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Game : Fragment() {

    private var _binding: GameBinding? = null
    val scoreModel: ScoreViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            scoreModel.setScore("3")
            findNavController().navigate(R.id.action_Game_to_Over)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}