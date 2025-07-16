package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.model.question.QuestionDetail
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getQuestions(limit: Int = 3): Flow<List<Question>>

    suspend fun getQuestionDetail(questionId: String): QuestionDetail

    fun getQuestionComments(questionId: String): Flow<List<QuestionComment>>

    suspend fun addComment(questionId: String, comment: String): QuestionComment

    suspend fun editComment(commentId: String, comment: String)

    suspend fun deleteComment(commentId: String)
}