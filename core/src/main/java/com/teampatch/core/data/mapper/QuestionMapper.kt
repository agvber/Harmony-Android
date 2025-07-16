package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.network.model.question.response.TodayQuestionResponse

fun TodayQuestionResponse.toDomain(index: Int): Question = Question(
    id = questionId.toString(),
    number = index,
    title = question
)