package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.QuestionComment
import com.teampatch.core.domain.model.QuestionDetail
import com.teampatch.core.data.network.model.question.response.QuestionCardCommentResponse
import com.teampatch.core.data.network.model.question.response.TodayQuestionResponse
import java.time.LocalDateTime

fun TodayQuestionResponse.toDomain(): QuestionDetail = QuestionDetail(
    id = questionId.toString(),
    number = 0,
    title = question,
    content = answer ?: "",
    dateTime = LocalDateTime.now(),
    commentCount = 0
)

fun QuestionCardCommentResponse.toDomain(): QuestionComment = QuestionComment(
    commentId = commentId.toString(),
    writerUid = authorId,
    writerName = authorId,
    content = content
)