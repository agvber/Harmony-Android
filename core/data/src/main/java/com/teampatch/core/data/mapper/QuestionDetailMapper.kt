package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.model.question.QuestionDetail
import com.teampatch.core.network.model.question.response.QuestionCardCommentResponse
import com.teampatch.core.network.model.question.response.TodayQuestionResponse
import java.time.LocalDateTime

fun TodayQuestionResponse.toDomain(): QuestionDetail = QuestionDetail(
    id = questionId.toString(),
    number = 0,
    title = question,
    content = answer ?: "",
    dateTime = LocalDateTime.now(),
)

fun QuestionCardCommentResponse.toDomain(): QuestionComment = QuestionComment(
    commentId = commentId.toString(),
    writerUid = authorId,
    writerName = authorId,
    content = content
)