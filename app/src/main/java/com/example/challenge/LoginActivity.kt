package com.example.challenge

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val senhaEditText = findViewById<EditText>(R.id.senhaEditText)
        val cadastrarButton = findViewById<Button>(R.id.cadastrarButton)
        val acessarButton = findViewById<Button>(R.id.acessarButton)
        val esqueciSenhaTextView = findViewById<TextView>(R.id.esqueciSenhaTextView)

        cadastrarButton.setOnClickListener {
            // Simula o cadastro com um Toast
            Toast.makeText(this, "Cadastro simulado", Toast.LENGTH_SHORT).show()
        }

        acessarButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                // Redireciona para a MainMenuActivity
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish() // Finaliza a LoginActivity para que o usuário não possa voltar a ela
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        esqueciSenhaTextView.setOnClickListener {
            Toast.makeText(this, "Esqueci minha senha", Toast.LENGTH_SHORT).show()
        }
    }
}
