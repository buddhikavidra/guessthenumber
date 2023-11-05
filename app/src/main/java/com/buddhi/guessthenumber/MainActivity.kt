package com.buddhi.guessthenumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var randomNumber: Int = (1..100).random()
    private var guessCount: Int = 1
    private var totalScore: Int = 0
    private var score: Int = 10
    private var timer: CountDownTimer? = null

    private lateinit var guessSubmit: Button
    private lateinit var guessField: EditText
    private lateinit var resultContainer: TextView
    private lateinit var timerContainer: TextView
    private lateinit var resetButton: Button
    private lateinit var totalScoreContainer: TextView
    private lateinit var myRange: SeekBar
    private lateinit var roundScoreContainer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guessSubmit = findViewById(R.id.submitGuess)
        guessField = findViewById(R.id.guessField)
        resultContainer = findViewById(R.id.resultText)
        timerContainer = findViewById(R.id.timerText)
        resetButton = findViewById(R.id.resetButton)
        totalScoreContainer = findViewById(R.id.totalScore)
        myRange = findViewById(R.id.guessSlider)
        roundScoreContainer = findViewById(R.id.roundScore)

        roundScoreContainer.text = "Current Round Score: $score"

        guessSubmit.setOnClickListener { checkGuess() }
        resetButton.setOnClickListener { resetGame() }

        startGame()
    }

        private fun startGame() {
            randomNumber = (1..100).random()
            guessCount = 1
            score = 10
            roundScoreContainer.text = "Current Round Score: $score"
            totalScoreContainer.text = "Total Score: $totalScore"
            guessField.isEnabled = true
            guessSubmit.isEnabled = true
            guessField.text.clear()
            guessField.requestFocus()
            resetButton.visibility = View.GONE
            resultContainer.text = ""
            startTimer()
        }

        private fun checkGuess() {
            val userGuess = guessField.text.toString().toIntOrNull()
            if (userGuess == null) {
                displayMessage("Please enter a valid number", "red")
                return
            }

            if (userGuess == randomNumber) {
                displayMessage("Congratulations! You got it right!", "green")
                totalScore += score
                totalScoreContainer.text = "Total Score: $totalScore"
                roundScoreContainer.text = ""
                endGame()
            } else {
                if (score > 1) {
                    score--
                    roundScoreContainer.text = "Current Round Score: $score"
                }
                if (userGuess < randomNumber) {
                    displayMessage("Last guess was too low!", "red")
                } else if (userGuess > randomNumber) {
                    displayMessage("Last guess was too high!", "red")
                }
            }
            val sliderValue = 100 - Math.abs(userGuess - randomNumber)
            myRange.progress = sliderValue
            guessCount++
            guessField.text.clear()
            guessField.requestFocus()
        }

        private fun displayMessage(msg: String, color: String) {
            resultContainer.text = msg
            resultContainer.setTextColor(android.graphics.Color.parseColor(color))
        }

        private fun endGame() {
            guessField.isEnabled = false
            guessSubmit.isEnabled = false
            resetButton.visibility = View.VISIBLE
            timer?.cancel()
        }

        private fun resetGame() {
            startGame()
        }

        private fun startTimer() {
            val time = 60
            timer = object : CountDownTimer((time * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerContainer.text = "Remaining time: ${(millisUntilFinished / 1000)} seconds"
                }

                override fun onFinish() {
                    displayMessage("Time is up! Try again.", "red")
                    endGame()
                }
            }.start()
        }
    }
