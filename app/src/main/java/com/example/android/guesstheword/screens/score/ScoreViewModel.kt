package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.screens.game.GameViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    // The buzz event
    private val _eventBuzz = MutableLiveData<Boolean>()
    var eventBuzz : LiveData<Boolean> = _eventBuzz

    private val _buzz = MutableLiveData<GameViewModel.BuzzType>()
    val buzz : LiveData<GameViewModel.BuzzType> = _buzz

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