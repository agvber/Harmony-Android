package com.teampatch.core.data.repository.local

import androidx.paging.PagingData
import com.harmony.core.database.LOCAL_DB_DATE_TIME_FORMATTER
import com.harmony.core.database.dao.QuestionDao
import com.harmony.core.database.dao.UserDao
import com.harmony.core.database.getCurrentTimeLocalDBFormat
import com.harmony.core.database.model.QuestionCommentEntity
import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.model.question.QuestionDetail
import com.teampatch.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

internal class LocalQuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val userDao: UserDao,
    private val authenticationLocalDatasource: AuthenticationLocalDatasource
) : QuestionRepository {

    override fun getQuestions(limit: Int): Flow<PagingData<Question>> =
        questionDao.getQuestions(limit).map { questionEntities ->
            questionEntities.map { questionEntity ->
                Question(
                    id = questionEntity.id.toString(),
                    number = 0,
                    title = questionEntity.title
                )
            }
                .let {
                    PagingData.from(it)
                }
        }

    override suspend fun getQuestionDetail(questionId: String): QuestionDetail {
        val question = questionDao.getQuestionById(questionId.toLong()).first()
        return QuestionDetail(
            id = question.id.toString(),
            number = 0,
            title = question.title,
            content = question.content,
            dateTime = LocalDateTime.parse(question.createdAt, LOCAL_DB_DATE_TIME_FORMATTER),
            commentCount = 0,
            comment = questionDao.getQuestionComments(questionId.toLong()).map { commentEntities ->
                commentEntities.map { commentEntity ->
                    QuestionComment(
                        commentId = commentEntity.comment.id.toString(),
                        writerUid = commentEntity.comment.writtenUid.toString(),
                        writerName = commentEntity.user.name,
                        content = commentEntity.comment.content
                    )
                }
                    .let { PagingData.from(it) }
            }
        )
    }

    override suspend fun addComment(questionId: String, comment: String): QuestionComment {
        val socialLoginId: String = authenticationLocalDatasource.getSocialLoginId()
        val user = userDao.getUserBySnsId(socialLoginId).first()
        val currentTime = getCurrentTimeLocalDBFormat()
        val questionCommentEntity = QuestionCommentEntity(
            questionId = questionId.toLong(),
            writtenUid = user.uid!!,
            content = comment,
            createdAt = currentTime,
            modifiedAt = currentTime
        )
        questionDao.insertQuestionComment(questionCommentEntity)

        return QuestionComment(
            commentId = questionCommentEntity.id.toString(),
            writerUid = questionCommentEntity.writtenUid.toString(),
            writerName = user.name,
            content = questionCommentEntity.content
        )
    }

    override suspend fun editComment(commentId: String, comment: String) {
        val questionCommentEntity = questionDao.getQuestionCommentById(commentId.toLong()).first()
        val currentTime = getCurrentTimeLocalDBFormat()
        questionDao.updateQuestionComment(
            questionCommentEntity.copy(
                content = comment,
                modifiedAt = currentTime
            )
        )
    }

    override suspend fun deleteComment(commentId: String) {
        questionDao.deleteQuestionComment(commentId.toLong())
    }
}