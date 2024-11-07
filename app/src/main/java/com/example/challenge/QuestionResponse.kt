package com.example.challenge

data class QuestionResponse(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correct_answer: String
)
