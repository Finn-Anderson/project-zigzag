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

    // Initialise variables
    private var _binding: GameBinding? = null
    private val binding get() = _binding!!

    val scoreModel: ScoreViewModel by activityViewModels()

    private var gameStart: Boolean = false

    private var x: Float = 0.0f
    private var y: Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Makes the side go back a page do nothing. Ideal world would be removing them completely
        requireActivity().onBackPressedDispatcher.addCallback { }

        // Initialise variables/values
        val gameLayout: ConstraintLayout = view.findViewById(R.id.gameLayout)

        scoreModel.setScore(0)

        val screenW = resources.displayMetrics.widthPixels
        val screenY = resources.displayMetrics.heightPixels

        var time: Long = 3000
        var yPos = 0.0f

        val handler = Handler(requireContext().mainLooper)

        // Gets circle location when absolutely created
        gameLayout.viewTreeObserver.addOnGlobalLayoutListener {
            _binding?.let {
                val location = IntArray(2)
                it.buttonGame.getLocationOnScreen(location)
                yPos = (location[1].toFloat())
            }
        }

        // Calls when circle touched
        binding.buttonGame.setOnTouchListener { v, event ->
            when (event?.action) {
                // On first touch, get circle initial x and y to use later
                MotionEvent.ACTION_DOWN -> {
                    x = v.x - event.rawX
                    y = v.y - event.rawY

                    // If the game has not started, start log creation loop
                    if (!gameStart) {
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

                    // Removes game instruction textview at the top-middle of the page
                    gameLayout.removeView(view.findViewById(R.id.gameInstructions))
                }
                // Sets circle's x and y location based on where user drags their finger
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

        // Timer calls 60x per second to check if user passed a log to gain points
        // Also checks if user has collided with a log. If they have, they are taken to the game over screen
        handler.postDelayed(object: Runnable {
            override fun run() {
                checkGainPoints(view, yPos, scoreModel)
                val state = onCollision(binding.buttonGame, gameLayout)
                if (state) {
                    handler.removeCallbacksAndMessages(null)
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