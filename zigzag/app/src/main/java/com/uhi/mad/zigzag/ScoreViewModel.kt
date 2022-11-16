package com.uhi.mad.zigzag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    private val score = MutableLiveData(0)

    // Sets the score based on the integer value passed
    fun setScore(score: Int) {
        this.score.value = score
    }

    // Gets the score as an integer
    fun getScore(): Int? {
        return score.value
    }
}