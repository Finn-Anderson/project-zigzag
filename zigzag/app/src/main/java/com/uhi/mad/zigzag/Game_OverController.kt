package com.uhi.mad.zigzag

import java.io.BufferedReader
import java.io.File

object GameOverController {

    fun storeDeviceHighscore(score: String?, path: File) {
        score?.let {
            val file = File(path, "score.txt")

            var highestScore = "0"
            if (file.exists()) {
                val bufferedReader: BufferedReader = file.bufferedReader()
                highestScore = bufferedReader.use { it.readText() }
            }

            if (score > highestScore) {
                file.writeText(score)
            }
        }
    }
}