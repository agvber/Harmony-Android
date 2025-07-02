package com.teampatch.feature.question.answer.model

import com.teampatch.core.domain.model.question.QuestionDetail
import java.time.LocalDateTime

internal data class AnswerUiState(
    val questionDetail: QuestionDetail = QuestionDetail(
        id = "",
        number = 0,
        title = "",
        content = "",
        dateTime = LocalDateTime.now(),
        commentCount = 0
    ),
    val isLoading: Boolean = true,
)