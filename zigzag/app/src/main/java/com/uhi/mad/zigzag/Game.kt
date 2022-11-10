package com.uhi.mad.zigzag

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
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

    private var x: Float = 0.0f
    private var y: Float = 0.0f
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback { }

        //binding.buttonGame.setOnClickListener {
            //scoreModel.setScore(3)
            //findNavController().navigate(R.id.action_Game_to_Over)
        //}

        binding.buttonGame.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = v.x - event.rawX
                    y = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.x = (event.rawX + x)
                    v.y = (event.rawY + y)

                    val screenW = resources.displayMetrics.widthPixels
                    val screenY = resources.displayMetrics.heightPixels

                    if (v.x < 0.0f) {
                        v.x = 0.0f
                    }
                    if (v.x > (screenW - v.width)) {
                        v.x = (screenW - v.width).toFloat()
                    }
                    if (v.y < 0.0f) {
                        v.y = 0.0f
                    }
                    if (v.y > (screenY - v.height - 66.0f)) {
                        v.y = (screenY - v.height - 66.0f)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}