package com.uhi.mad.zigzag

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import java.util.*

// Initialise variables
var count: Int = 0
var scoreTally: MutableList<Boolean> = mutableListOf()

/**
 * Moves the obstacles down by 3 pixels every 1/60 seconds
 *
 * @property ctx Context
 * @property logL left log view
 * @property logR right log view
 * @property parent game layout
 * @property height height of the device. Used to remove logs when it reaches the bottom of it
 */
private fun moveObstacle(ctx: Context, logL: View, logR: View, parent: ConstraintLayout, height: Int) {
    val handler = Handler(ctx.mainLooper)
    handler.postDelayed(object: Runnable {
        override fun run() {
            logL.y += 3
            logR.y += 3

            if (logR.y > height || logL.y > height) {
                parent.removeView(logL)
                parent.removeView(logR)
            } else {
                handler.postDelayed(this, 1000/60)
            }
        }
    }, 0)
}

/**
 * Creates varying log rows that user must dodge and pass once to earn point
 * Generates two logs. One from left and one the from right side of the screen
 *
 * @property ctx Context
 * @property parent layout logs reside in (game layout)
 * @property resources Resources
 * @property display used to get display width for log width variation
 */
fun createObstacle(ctx: Context, parent: ConstraintLayout, resources: Resources, display: DisplayMetrics) {
    val width = display.widthPixels
    val getW = width - Random().nextInt(width - 0)
    count += 1

    val logLeft = View(ctx)
    logLeft.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle, null)!!
    logLeft.layoutParams = ConstraintLayout.LayoutParams((getW - 200), 88)
    logLeft.id = count

    val logRight = View(ctx)
    logRight.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle, null)!!
    logRight.layoutParams = ConstraintLayout.LayoutParams((width - getW - 100), 88)
    logRight.x = (width - (width - getW - 100)).toFloat()
    logRight.id = count

    parent.addView(logLeft)
    parent.addView(logRight)

    scoreTally.add(false)

    moveObstacle(ctx, logLeft, logRight, parent, display.heightPixels)
}

/**
 * Checks if the user's circle has passed a log row with the value false before adding a point
 *
 * @property v game view
 * @property y float of circle's y value
 * @property scoreModel used to add score when the circle passes a log row
 */
fun checkGainPoints(v: View, y: Float, scoreModel: ScoreViewModel) {
    val index: Int = scoreTally.indexOfFirst { !it }

    // Checks if the scoreTally contains a log that has false and gets it's index
    // This is used to represent passing a specific log row only accounts for 1 score
    if (index > -1) {
        val id = v.findViewById<View>((index + 1))

        if (y < id.y) {
            scoreTally[index] = true
            var curScore: Int? = scoreModel.getScore()
            if (curScore == null) {
                curScore = 0
            }
            scoreModel.setScore(curScore + 1)
        }
    }
}

/**
 * Checks if the user's circle has collided with a log
 * If the user collides with a log, reset values and return true (end game)
 *
 * @property circle view of the circle
 * @property parent game layout
 * @return returns collision state as boolean
 */
fun onCollision(circle: View, parent: ConstraintLayout): Boolean {
    val children = parent.childCount
    var i = 0
    var state = false

    // Checks all log area's to see if they intersect with the circle's area
    repeat(children) {
        val childView = parent.getChildAt(i)
        if (childView != circle) {
            val circleRect = Rect()
            val childRect = Rect()

            circle.getHitRect(circleRect)
            childView.getHitRect(childRect)

            if (circleRect.intersect(childRect)) {
                state = true
                scoreTally.clear()
                count = 0
            }
        }
        i += 1
    }
    return state
}
