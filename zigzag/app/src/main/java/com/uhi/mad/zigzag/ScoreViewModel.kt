package com.uhi.mad.zigzag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    private val score = MutableLiveData<String>()

    fun setScore(score: String) {
        this.score.value = score
    }

    fun getScore(): String? {
        return score.value
    }
}