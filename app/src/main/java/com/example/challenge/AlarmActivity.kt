package com.example.challenge

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val setAlarmButton = findViewById<Button>(R.id.setAlarmButton)
        val timeTextView = findViewById<TextView>(R.id.timeTextView) // Para mostrar o horário da API
        val getCurrentTimeButton = findViewById<Button>(R.id.getCurrentTimeButton)

        timePicker.setIs24HourView(true)

        // Define o horário usando TimePicker
        setAlarmButton.setOnClickListener {
            val hour: Int
            val minute: Int

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timePicker.hour
                minute = timePicker.minute
            } else {
                hour = timePicker.currentHour
                minute = timePicker.currentMinute
            }

            Toast.makeText(this, "Alarme definido para: $hour:$minute", Toast.LENGTH_SHORT).show()
        }

        // Obter o horário atual da API e exibir no TextView
        getCurrentTimeButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    // Aqui, usamos RetrofitClientWorldTime para obter o horário
                    val response = RetrofitClientWorldTime.instance.getTime("America", "Sao_Paulo")
                    if (response.isSuccessful) {
                        val time = response.body()?.datetime
                        timeTextView.text = "Horário atual em São Paulo: $time"
                    } else {
                        Toast.makeText(this@AlarmActivity, "Erro ao obter horário", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AlarmActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupAlarm(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // Lógica para definir um alarme real pode ser adicionada aqui com AlarmManager
    }
}
