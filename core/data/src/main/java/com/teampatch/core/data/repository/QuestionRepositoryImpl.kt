package com.teampatch.core.data.repository

import androidx.paging.PagingData
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.model.Question
import com.teampatch.core.domain.model.QuestionComment
import com.teampatch.core.domain.model.QuestionDetail
import com.teampatch.core.domain.repository.QuestionRepository
import com.teampatch.core.domain.repository.UserRepository
import com.teampatch.core.data.network.QuestionRemoteDataSource
import com.teampatch.core.data.network.model.question.request.CommentRequestBody
import com.teampatch.core.data.network.model.question.request.QuestionCardCommentRequestBody
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class QuestionRepositoryImpl @Inject constructor(
    private val questionRemoteDataSource: QuestionRemoteDataSource,
    private val userRepository: UserRepository,
) : QuestionRepository {

    override fun getQuestions(limit: Int): Flow<PagingData<Question>> = flow {
        val user = userRepository.getUserInfo().first()
        val questions = if ((1..3).contains(limit)) {
            questionRemoteDataSource.getRecentThreeQuestions(user.groupId)
        } else {
            questionRemoteDataSource.getQuestionAll(user.groupId)
        }
            .data
            .let { if (limit > 0) it.subList(0, limit) else it }
            .mapIndexed { index, question ->
                question.toDomain(index)
            }
        emit(PagingData.from(questions))
    }

    override suspend fun getQuestionDetail(questionId: String): QuestionDetail {
        val questionResponse = questionRemoteDataSource.getQuestionDetail(questionId = questionId.toInt()).data
        val commentResponse = questionRemoteDataSource.getQuestionCardComments(questionId.toInt()).data
        val comment = commentResponse.map { it.toDomain() }

        return questionResponse
            .toDomain()
            .copy(commentCount = commentResponse.size, comment = flowOf(PagingData.from(comment)))
    }

    override suspend fun addComment(questionId: String, comment: String): QuestionComment {
        val user = userRepository.getUserInfo().first()
        val questionCardCommentRequestBody =
            QuestionCardCommentRequestBody(questionId.toInt(), user.groupId, user.name, comment)
        val commentCreateResponse = questionRemoteDataSource.postQuestionCardComment(questionCardCommentRequestBody)
        val commentCreateResponseData = commentCreateResponse.data
        return QuestionComment(commentCreateResponseData.commentId.toString(), user.uid, user.name, comment)
    }

    override suspend fun editComment(commentId: String, comment: String) {
        val commentRequestBody = CommentRequestBody(comment)
        questionRemoteDataSource.putComment(commentId.toInt(), commentRequestBody)
    }

    override suspend fun deleteComment(commentId: String) {
        questionRemoteDataSource.deleteComment(commentId.toInt())
    }
}