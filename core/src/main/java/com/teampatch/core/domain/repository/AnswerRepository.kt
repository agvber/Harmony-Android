package com.teampatch.core.domain.repository

interface AnswerRepository {

    suspend fun addAnswer(questionId: String, answer: String)

    suspend fun editAnswer(questionId: String, answer: String)
}