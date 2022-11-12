package com.uhi.mad.zigzag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    private val score = MutableLiveData(0)

    fun setScore(score: Int) {
        this.score.value = score
    }

    fun getScore(): Int? {
        return score.value
    }
}