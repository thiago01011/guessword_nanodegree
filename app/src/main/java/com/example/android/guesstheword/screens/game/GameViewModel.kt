package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the time when the phone will start buzzing each second
        const val COUNTDOWN_PANIC_SECONDS = 10L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int> = _score

    // The finish game status
    private val _eventFinishedGame = MutableLiveData<Boolean>()
    var eventFinishedGame : LiveData<Boolean> = _eventFinishedGame

    // The buzz event
    private val _eventBuzz = MutableLiveData<Boolean>()
    var eventBuzz : LiveData<Boolean> = _eventBuzz

    // The buzz event
    private val _buzz = MutableLiveData<BuzzType>()
    var buzz : LiveData<BuzzType> = _buzz

    private var timer : CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long> = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created.")
        resetList()

        _score.value = 0
        _eventFinishedGame.value = false
        Log.i("GameViewModel", "Creating buzz 0.")

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
                var time = _currentTime.value
                if ((millisUntilFinished / ONE_SECOND) <= COUNTDOWN_PANIC_SECONDS) {
                    _buzz.value = BuzzType.COUNTDOWN_PANIC
                    _eventBuzz.value = true
                }
                if (time?.equals(0)?:true) {
                    if (_score?.value?:0 <= 0) {
                        _buzz.value = BuzzType.GAME_OVER
                        _eventBuzz.value = true
                    }
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventFinishedGame.value = true
            }
        }

        timer.start()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.i("GameViewModel", "GameViewModel destroyed.")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        Log.i("GameViewModel", "Correct buzz Correct.")
        _buzz.value = BuzzType.CORRECT
        _eventBuzz.value = true
        nextWord()
    }

    fun onFinishedBuzzEvent() {
        _eventBuzz.value = false
        _buzz.value = BuzzType.NO_BUZZ
    }

    fun onGameOver() {
        _eventBuzz.value = true
        _buzz.value = BuzzType.GAME_OVER
    }

    fun onFinishedGameEvent() {
        _eventFinishedGame.value = false
    }
}