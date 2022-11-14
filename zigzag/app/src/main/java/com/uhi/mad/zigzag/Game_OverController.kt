package com.uhi.mad.zigzag

import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.io.File

object GameOverController {

    fun storeDeviceHighscore(score: String?, activity: FragmentActivity?, path: File) {
        score?.let {
            val file = File(path, "score.txt")

            var highestScore = "0"
            if (file.exists()) {
                val bufferedReader: BufferedReader = file.bufferedReader()
                highestScore = bufferedReader.use { it.readText() }
            }

            if (highestScore == "null" || score.toInt() > highestScore.toInt()) {
                file.writeText(score)
                (activity as MainActivity).notification("High Score!", "You have achieved a new high score!")
            }
        }
    }
}