package com.example.startwithkotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    // TODO: Kann ich das auch ohne eine globale Variable machen?
    private var currentanswer: Float = 0.toFloat()
    private var totalAttempts: Int = 0
    private var correctAttempts: Int = 0
    private var symbol: Int = 0



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        answerButton1.text = "Are"
        answerButton2.text = "you"
        answerButton3.text = "ready"
        answerButton4.text = "?"

        standardButton.setOnClickListener{

            startButton.setOnClickListener {
                //Toast.makeText(this@MainActivity, "You clicked me", Toast.LENGTH_LONG).show()

                updateAnswerBoxes(updateQuestion())
                startingSettings(clickListenerStandard)
            }
        }

        symbolsButton.setOnClickListener{

            startButton.setOnClickListener {
                //Toast.makeText(this@MainActivity, "You clicked me", Toast.LENGTH_LONG).show()

                answerButton1.text = "+"
                answerButton2.text = "-"
                answerButton3.text = "*"
                answerButton4.text = "/"

                updateQuestionSymbols()
                startingSettings(clickListenerSymbol)
            }
        }
    }

    /*
    Input: Nimmt den jewiligen ClickListener für die Antwortboxen und dementsprechend darauf zu reagieren.
    Macht den Teil am Anfang, der für beide Varianten gemacht werden muss. Code kann eingespart werden.
     */
    @SuppressLint("SetTextI18n")
    private fun startingSettings(cL : View.OnClickListener) {
        answerButton1.setOnClickListener(cL)
        answerButton2.setOnClickListener(cL)
        answerButton3.setOnClickListener(cL)
        answerButton4.setOnClickListener(cL)

        startButton.isClickable = false
        answerButton1.isClickable = true
        answerButton2.isClickable = true
        answerButton3.isClickable = true
        answerButton4.isClickable = true
        standardButton.isClickable = false
        symbolsButton.isClickable = false

        totalAttempts = 0
        correctAttempts = 0
        textViewResult.text = "$correctAttempts / $totalAttempts"
        timer.start()
    }


    /*
    fun: updateQuestionsSymbolds

    Erstellt eine neue Frage und fügt diese in das Textfeld ein

    return: Die Zahl, welches Symbold benutzt wurde
    Array mit den Symbolen; 1: +, 2: -, 3: *, 4: /
     */
    @SuppressLint("SetTextI18n")
    private fun updateQuestionSymbols(){
        val digitOne = (1..49).random().toFloat()
        val digitTwo = (1..49).random().toFloat()
        symbol = (1..4).random()

        currentanswer = when (symbol) {
            1 ->  digitOne + digitTwo
            2 ->  digitOne - digitTwo
            3 ->  digitOne * digitTwo
            4 ->  digitOne / digitTwo
            else -> (-1).toFloat()
        }

        val num = currentanswer
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        currentanswer = (df.format(num)).toFloat()

        this.textViewQuestion.text = "${digitOne.toInt()} ? ${digitTwo.toInt()} = $currentanswer"


    }



    /*
    fun : updateAnswerBoxes
    updates all possible answerboxes and sets on box with the correct answer, all others have different results which are set wrongly.
    return: Number of the box with the correct answer: 1: upper left, 2: upper right, 3 lower left, 4: lower right
     */
    private fun updateAnswerBoxes (answer : Int) : Int{
        val correctBox =  (0..3).random()
        val x = IntArray(4)

        for(i in 0..3) {
            var wrongAnswer = (answer-5 ..answer+5).random()
            if( i == correctBox) {
                x[i] = answer
            } else {
                while( wrongAnswer == answer || x.contains(wrongAnswer) || wrongAnswer <= 0 || wrongAnswer >= 100 ) {
                    wrongAnswer = (answer-5 ..answer+5).random()
                }
                x[i] = wrongAnswer
            }
        }

        answerButton1.text = x[0].toString()
        answerButton2.text = x[1].toString()
        answerButton3.text = x[2].toString()
        answerButton4.text = x[3].toString()


        return correctBox+1

    }



    /*
    fun : updateQuestion
    creates a new equation and sets the on the QuestionTextView
    return: (int) - correct answer
     */
    @SuppressLint("SetTextI18n")
    fun updateQuestion() : Int {

        val digitOne = (1..49).random()
        val digitTwo = (1..49).random()

        this.textViewQuestion.text = "$digitOne + $digitTwo"
        currentanswer = digitOne.toFloat() + digitTwo.toFloat()
        return currentanswer.toInt()
        }




    @SuppressLint("SetTextI18n")
    private val clickListenerStandard = View.OnClickListener { view ->

        val usersAnswer = findViewById<Button>(view.id).text
        if(usersAnswer == currentanswer.toInt().toString()) {

            //Toast.makeText(this@MainActivity, "correct", Toast.LENGTH_LONG).show()
            correctAttempts++

        } else {
            //Toast.makeText(this@MainActivity, "wrong", Toast.LENGTH_LONG).show()
        }
        totalAttempts++
        textViewResult.text = "$correctAttempts / $totalAttempts"


        updateAnswerBoxes(updateQuestion())

    }

    @SuppressLint("SetTextI18n")
    private val clickListenerSymbol = View.OnClickListener { view ->

        val usersAnswer = findViewById<Button>(view.id).text
        val correctAnswer = when(symbol) {
            1-> "+"
            2-> "-"
            3-> "*"
            4-> "/"
            else -> ""
        }
        if(usersAnswer == correctAnswer) {
            correctAttempts++
        }

        totalAttempts++
        textViewResult.text = "$correctAttempts / $totalAttempts"
        updateQuestionSymbols()
    }



    private val timer = object: CountDownTimer(30000, 1000) {


        override fun onTick(millisUntilFinished: Long) {


              textViewTimer.text = (millisUntilFinished/1000).toInt().toString()
        }

        @SuppressLint("SetTextI18n")
        override fun onFinish() {
            textViewTimer.text  = "30"
            Toast.makeText(this@MainActivity, "Game over", Toast.LENGTH_LONG).show()


            answerButton1.isClickable = false
            answerButton2.isClickable = false
            answerButton3.isClickable = false
            answerButton4.isClickable = false
            startButton.isClickable = true
            standardButton.isClickable = true
            symbolsButton.isClickable = true

            startButton.text = "Start again"
            answerButton1.text = ""
            answerButton2.text = ""
            answerButton3.text = ""
            answerButton4.text = ""
            textViewQuestion.text = ""



        }


    }




    }

