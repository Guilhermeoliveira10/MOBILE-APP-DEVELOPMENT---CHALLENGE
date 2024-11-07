package com.example.challenge

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DoubtsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doubts)

        val btnToothCare = findViewById<Button>(R.id.btnToothCare)
        val btnGoodHabits = findViewById<Button>(R.id.btnGoodHabits)
        val btnDentalFloss = findViewById<Button>(R.id.btnDentalFloss)
        val btnToothache = findViewById<Button>(R.id.btnToothache)
        val adviceTextView = findViewById<TextView>(R.id.adviceTextView)

        btnToothCare.setOnClickListener { loadAdvice("toothCare", adviceTextView) }
        btnGoodHabits.setOnClickListener { loadAdvice("goodHabits", adviceTextView) }
        btnDentalFloss.setOnClickListener { loadAdvice("dentalFloss", adviceTextView) }
        btnToothache.setOnClickListener { loadAdvice("toothache", adviceTextView) }
    }

    private fun loadAdvice(category: String, adviceTextView: TextView) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClientAdvice.instance.getAdvice(category)
                if (response.isSuccessful) {
                    val advice = response.body()?.advice
                    adviceTextView.text = advice ?: "Nenhum conselho disponível"
                } else {
                    adviceTextView.text = "Erro ao carregar conselho da API."
                }
            } catch (e: Exception) {
                adviceTextView.text = "Erro: ${e.message}"
            }
        }
    }
}
