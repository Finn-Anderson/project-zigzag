package com.uhi.mad.zigzag

import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.io.File

object GameOverController {

    /**
     * Saves the highest score set on this device and creates a notification
     *
     * @property score string to save to score.txt if it is higher than the one already stored
     * @property activity used to call notification() in MainActivity
     * @property path file directory
     */
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