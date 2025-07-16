package com.teampatch.core.data.repository

import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.model.question.QuestionDetail
import com.teampatch.core.domain.repository.QuestionRepository
import com.teampatch.core.domain.repository.UserRepository
import com.teampatch.core.network.QuestionRemoteDataSource
import com.teampatch.core.network.model.question.request.CommentRequestBody
import com.teampatch.core.network.model.question.request.QuestionCardCommentRequestBody
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class QuestionRepositoryImpl @Inject constructor(
    private val questionRemoteDataSource: QuestionRemoteDataSource,
    private val userRepository: UserRepository,
) : QuestionRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getQuestions(limit: Int): Flow<List<Question>> = userRepository.getUserInfo()
        .distinctUntilChanged { old, new -> old.groupId == new.groupId }
        .map { user ->
            if (limit in QUESTION_MIN_RANGE) {
                questionRemoteDataSource.getRecentThreeQuestions(user.groupId)
            } else {
                questionRemoteDataSource.getQuestionAll(user.groupId)
            }
                .data
                .let { if (limit > 0) it.subList(0, limit) else it }
                .mapIndexed { index, question ->
                    question.toDomain(index)
                }
        }

    override suspend fun getQuestionDetail(questionId: String): QuestionDetail {
        return questionRemoteDataSource.getQuestionDetail(questionId = questionId.toInt())
            .data
            .toDomain()
    }

    override fun getQuestionComments(questionId: String): Flow<List<QuestionComment>> = flow {
        questionRemoteDataSource.getQuestionCardComments(questionId.toInt()).data
            .map { it.toDomain() }
            .let { emit(it) }
    }

    override suspend fun addComment(questionId: String, comment: String): QuestionComment {
        val user = userRepository.getUserInfo().first()
        val questionCardCommentRequestBody =
            QuestionCardCommentRequestBody(questionId.toInt(), user.groupId, user.name, comment)
        val commentCreateResponse =
            questionRemoteDataSource.postQuestionCardComment(questionCardCommentRequestBody)
        val commentCreateResponseData = commentCreateResponse.data
        return QuestionComment(
            commentId = commentCreateResponseData.commentId.toString(),
            writerUid = user.uid,
            writerName = user.name,
            content = comment
        )
    }

    override suspend fun editComment(commentId: String, comment: String) {
        val commentRequestBody = CommentRequestBody(comment)
        questionRemoteDataSource.putComment(commentId.toInt(), commentRequestBody)
    }

    override suspend fun deleteComment(commentId: String) {
        questionRemoteDataSource.deleteComment(commentId.toInt())
    }

    companion object {
        private val QUESTION_MIN_RANGE: IntRange = 0..3
    }
}