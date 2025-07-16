package com.teampatch.core.data.repository

import com.teampatch.core.domain.repository.AnswerRepository
import com.teampatch.core.network.QuestionRemoteDataSource
import com.teampatch.core.network.model.question.request.QuestionCardAnswerRequestBody
import javax.inject.Inject

class AnswerRepositoryImpl @Inject constructor(
    private val questionRemoteDataSource: QuestionRemoteDataSource,
) : AnswerRepository {

    override suspend fun addAnswer(questionId: String, answer: String) {
        val questionCardAnswerRequestBody = QuestionCardAnswerRequestBody(answer)
        questionRemoteDataSource.postQuestionCardAnswer(
            questionId = questionId.toInt(),
            questionCardAnswerRequestBody = questionCardAnswerRequestBody
        )
    }

    override suspend fun editAnswer(questionId: String, answer: String) {
        val questionCardAnswerRequestBody = QuestionCardAnswerRequestBody(answer)
        questionRemoteDataSource.putQuestionCardAnswer(
            questionId = questionId.toInt(),
            questionCardAnswerRequestBody = questionCardAnswerRequestBody
        )
    }
}