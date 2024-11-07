package com.example.challenge

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private lateinit var questions: List<QuestionResponse>
    private val userAnswers = mutableListOf<Pair<String, Boolean>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val questionTextView = findViewById<TextView>(R.id.questionTextView)
        val answersRadioGroup = findViewById<RadioGroup>(R.id.answersRadioGroup)
        val submitAnswerButton = findViewById<Button>(R.id.submitAnswerButton)
        val resultsLayout = findViewById<LinearLayout>(R.id.resultsLayout)

        loadQuestions()

        submitAnswerButton.setOnClickListener {
            val selectedOptionId = answersRadioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
                val answer = selectedRadioButton.text.toString()
                val isCorrect = checkAnswer(answer)

                userAnswers.add(Pair(answer, isCorrect))

                answersRadioGroup.clearCheck()

                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    displayQuestion(questions[currentQuestionIndex])
                } else {
                    displayResults(resultsLayout)
                }
            } else {
                Toast.makeText(this, "Por favor, selecione uma resposta.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadQuestions() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClientQuiz.instance.getQuestions()
                if (response.isSuccessful) {
                    questions = response.body() ?: emptyList()
                    if (questions.isNotEmpty()) {
                        displayQuestion(questions[currentQuestionIndex])
                    } else {
                        Toast.makeText(this@QuizActivity, "Nenhuma pergunta disponível", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@QuizActivity, "Erro ao carregar perguntas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@QuizActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayQuestion(question: QuestionResponse) {
        val questionTextView = findViewById<TextView>(R.id.questionTextView)
        val answersRadioGroup = findViewById<RadioGroup>(R.id.answersRadioGroup)

        questionTextView.text = question.question
        answersRadioGroup.removeAllViews()

        question.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this)
            radioButton.text = option
            answersRadioGroup.addView(radioButton)
        }
    }

    private fun checkAnswer(selectedAnswer: String): Boolean {
        val correctAnswer = questions[currentQuestionIndex].correct_answer
        return selectedAnswer == correctAnswer
    }

    private fun displayResults(resultsLayout: LinearLayout) {
        resultsLayout.removeAllViews()
        userAnswers.forEachIndexed { index, answer ->
            val question = questions[index]
            val resultView = LinearLayout(this)
            resultView.orientation = LinearLayout.VERTICAL
            resultView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            resultView.setPadding(16, 16, 16, 16)

            val answerText = TextView(this)
            answerText.text = "${index + 1} - Sua Resposta: ${answer.first}"

            val correctAnswerText = TextView(this)
            correctAnswerText.text = "Resposta Correta: ${questions[index].correct_answer}"

            resultView.addView(answerText)
            resultView.addView(correctAnswerText)

            resultsLayout.addView(resultView)
        }

        resultsLayout.visibility = View.VISIBLE
    }
}
