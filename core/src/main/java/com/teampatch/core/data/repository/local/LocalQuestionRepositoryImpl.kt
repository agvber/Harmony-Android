package com.teampatch.core.data.repository.local

import com.teampatch.core.database.LOCAL_DB_DATE_TIME_FORMATTER
import com.teampatch.core.database.dao.QuestionDao
import com.teampatch.core.database.dao.UserDao
import com.teampatch.core.database.getCurrentTimeLocalDBFormat
import com.teampatch.core.database.model.QuestionCommentEntity
import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.model.question.QuestionDetail
import com.teampatch.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

internal class LocalQuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val userDao: UserDao,
    private val authenticationLocalDatasource: AuthenticationLocalDatasource
) : QuestionRepository {

    override fun getQuestions(limit: Int): Flow<List<Question>> =
        questionDao.getQuestions(limit).map { questionEntities ->
            questionEntities.map { questionEntity ->
                Question(
                    id = questionEntity.id.toString(),
                    number = questionEntity.id.toInt(),
                    title = questionEntity.title
                )
            }
        }

    override suspend fun getQuestionDetail(questionId: String): QuestionDetail {
        return questionDao.getQuestionById(questionId.toLong()).map { question ->
            QuestionDetail(
                id = question.id.toString(),
                number = question.id.toInt(),
                title = question.title,
                content = question.content,
                dateTime = LocalDateTime.parse(question.createdAt, LOCAL_DB_DATE_TIME_FORMATTER),
            )
        }
            .first()
    }

    override fun getQuestionComments(questionId: String): Flow<List<QuestionComment>> {
        return questionDao.getQuestionComments(questionId.toLong()).map { commentEntities ->
            commentEntities.map { commentEntity ->
                QuestionComment(
                    commentId = commentEntity.comment.id.toString(),
                    writerUid = commentEntity.comment.writtenUid.toString(),
                    writerName = commentEntity.user.name,
                    content = commentEntity.comment.content
                )
            }
        }
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
        val currentTime = getCurrentTimeLocalDBFormat()
        questionDao.getQuestionCommentById(commentId.toLong()).onEach {
            questionDao.updateQuestionComment(
                it.copy(
                    content = comment,
                    modifiedAt = currentTime
                )
            )
        }
            .first()
    }

    override suspend fun deleteComment(commentId: String) {
        questionDao.deleteQuestionComment(commentId.toLong())
    }
}