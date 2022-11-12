package com.uhi.mad.zigzag

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.uhi.mad.zigzag.databinding.GameBinding
import androidx.navigation.fragment.findNavController
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Game : Fragment() {

    private var _binding: GameBinding? = null

    val scoreModel: ScoreViewModel by activityViewModels()

    private var gameStart: Boolean = false

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

        val gameLayout: ConstraintLayout = view.findViewById(R.id.gameLayout)

        val screenW = resources.displayMetrics.widthPixels
        val screenY = resources.displayMetrics.heightPixels

        var time: Long = 3000
        var yPos = 0.0f

        gameLayout.viewTreeObserver.addOnGlobalLayoutListener {
            _binding?.let {
                val location = IntArray(2)
                it.buttonGame.getLocationOnScreen(location)
                yPos = (location[1].toFloat())
            }
        }

        binding.buttonGame.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = v.x - event.rawX
                    y = v.y - event.rawY

                    if (!gameStart) {
                        val handler = Handler(requireContext().mainLooper)
                        handler.postDelayed(object: Runnable {
                            override fun run() {
                                createObstacle(requireContext(), gameLayout, resources, resources.displayMetrics)
                                handler.postDelayed(this, time)
                                if (time > 1500) {
                                    time -= 50
                                }
                            }
                        }, 0)

                        gameStart = true
                    }

                    gameLayout.removeView(view.findViewById(R.id.gameInstructions))
                }
                MotionEvent.ACTION_MOVE -> {
                    v.x = (event.rawX + x)
                    v.y = (event.rawY + y)

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

                    yPos = v.y
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        val handler = Handler(requireContext().mainLooper)
        handler.postDelayed(object: Runnable {
            override fun run() {
                checkGainPoints(view, yPos, scoreModel)
                val state = onCollision(binding.buttonGame, gameLayout)
                if (state) {
                    findNavController().navigate(R.id.action_Game_to_Over)
                } else {
                    handler.postDelayed(this, 1000/60)
                }
            }
        }, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}