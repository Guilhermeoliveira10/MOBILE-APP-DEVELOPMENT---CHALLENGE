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
    private val userAnswers = mutableListOf<Pair<String, Boolean>>() // Armazenar respostas do usuário

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val questionTextView = findViewById<TextView>(R.id.questionTextView)
        val answersRadioGroup = findViewById<RadioGroup>(R.id.answersRadioGroup)
        val submitAnswerButton = findViewById<Button>(R.id.submitAnswerButton)
        val resultsLayout = findViewById<LinearLayout>(R.id.resultsLayout) // LinearLayout para resultados

        // Fazer a requisição de perguntas da API
        loadQuestions()

        // Configuração do botão de envio
        submitAnswerButton.setOnClickListener {
            val selectedOptionId = answersRadioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
                val answer = selectedRadioButton.text.toString()
                val isCorrect = checkAnswer(answer)

                // Armazenar a resposta do usuário
                userAnswers.add(Pair(answer, isCorrect))

                // Limpar a seleção após enviar
                answersRadioGroup.clearCheck()

                // Passar para a próxima pergunta
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
        answersRadioGroup.removeAllViews() // Limpa o RadioGroup antes de adicionar novas opções

        // Adiciona as opções de resposta em ordem sem duplicação
        question.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this)
            radioButton.text = option // Mantém a formatação original da opção
            answersRadioGroup.addView(radioButton)
        }
    }

    private fun checkAnswer(selectedAnswer: String): Boolean {
        val correctAnswer = questions[currentQuestionIndex].correct_answer
        return selectedAnswer == correctAnswer
    }

    private fun displayResults(resultsLayout: LinearLayout) {
        resultsLayout.removeAllViews() // Limpa os resultados antes de exibir novos
        userAnswers.forEachIndexed { index, answer ->
            val question = questions[index]
            val resultView = LinearLayout(this)
            resultView.orientation = LinearLayout.VERTICAL
            resultView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            resultView.setPadding(16, 16, 16, 16) // Adiciona espaçamento entre os resultados

            // Formata a resposta
            val answerText = TextView(this)
            answerText.text = "${index + 1} - Sua Resposta: ${answer.first}" // Número da pergunta e resposta do usuário

            // Adiciona a resposta correta
            val correctAnswerText = TextView(this)
            correctAnswerText.text = "Resposta Correta: ${questions[index].correct_answer}"

            resultView.addView(answerText)
            resultView.addView(correctAnswerText)

            resultsLayout.addView(resultView)
        }

        resultsLayout.visibility = View.VISIBLE // Certifique-se de que o layout de resultados esteja visível
    }
}
