package com.uhi.mad.zigzag

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.math.MathUtils
import java.util.*

var time: Long = (1000/60)

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
                handler.postDelayed(this, time)
            }
        }
    }, 0)
}

fun createObstacle(ctx: Context, parent: ConstraintLayout, resources: Resources, display: DisplayMetrics) {
    val width = display.widthPixels
    val getW = width - Random().nextInt(width - 0)

    val logLeft = View(ctx)
    logLeft.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle, null)!!
    logLeft.layoutParams = ConstraintLayout.LayoutParams((getW - 200), 88)

    val logRight = View(ctx)
    logRight.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle, null)!!
    logRight.layoutParams = ConstraintLayout.LayoutParams((width - getW - 100), 88)
    logRight.x = (width - (width - getW - 100)).toFloat()

    parent.addView(logLeft)
    parent.addView(logRight)

    moveObstacle(ctx, logLeft, logRight, parent, display.heightPixels)
}