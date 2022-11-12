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

var count: Int = 0
var scoreTally: Array<Boolean> = arrayOf()

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

    scoreTally += false

    moveObstacle(ctx, logLeft, logRight, parent, display.heightPixels)
}

fun checkGainPoints(v: View, y: Float, scoreModel: ScoreViewModel) {
    val index: Int = scoreTally.indexOfFirst { !it }

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

fun onCollision(circle: View, parent: ConstraintLayout): Boolean {
    val children = parent.childCount
    var i = 0
    var state = false
    repeat(children) {
        val childView = parent.getChildAt(i)
        if (childView != circle) {
            val circleRect = Rect()
            val childRect = Rect()

            circle.getHitRect(circleRect)
            childView.getHitRect(childRect)

            if (circleRect.intersect(childRect)) {
                state = true
            }
        }
        i += 1
    }
    return state
}
