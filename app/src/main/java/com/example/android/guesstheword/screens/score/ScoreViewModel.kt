package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int> = _score

    private val _eventPlayed = MutableLiveData<Boolean>()
    val eventPlayed : LiveData<Boolean> = _eventPlayed

    init {
        Log.i("ScoreViewModel", "Score: ${finalScore}")
        _eventPlayed.value = false
        _score.value = finalScore
    }

    fun playAgain() {
        _eventPlayed.value = true
    }

    fun resetEventPlayedStatus() {
        _eventPlayed.value = false
    }

}