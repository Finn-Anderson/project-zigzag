package com.uhi.mad.zigzag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsernameViewModel: ViewModel() {
    private val name = MutableLiveData<String>()

    fun setName(name: String) {
        this.name.value = name
    }

    fun getName(): String? {
        return name.value
    }
}