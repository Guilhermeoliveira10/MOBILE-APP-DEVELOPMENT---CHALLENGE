package com.example.challenge

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val quizButton = findViewById<Button>(R.id.btnQuiz)
        val alarmButton = findViewById<Button>(R.id.btnAlarm)
        val doubtsButton = findViewById<Button>(R.id.btnDoubts)

        quizButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        alarmButton.setOnClickListener {
            startActivity(Intent(this, AlarmActivity::class.java))
        }

        doubtsButton.setOnClickListener {
            startActivity(Intent(this, DoubtsActivity::class.java))
        }
    }
}
