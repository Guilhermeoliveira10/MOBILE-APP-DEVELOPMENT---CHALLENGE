package com.example.challenge

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("timezone/{region}/{city}")
    suspend fun getTime(
        @Path("region") region: String,
        @Path("city") city: String
    ): Response<TimeResponse>

    @GET("advice/{category}")
    suspend fun getAdvice(@Path("category") category: String): Response<AdviceResponse>

    @GET("questions")
    suspend fun getQuestions(): Response<List<QuestionResponse>>
}
