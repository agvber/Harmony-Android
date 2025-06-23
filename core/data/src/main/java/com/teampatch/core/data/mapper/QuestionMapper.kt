package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.Question
import com.teampatch.core.data.network.model.question.response.TodayQuestionResponse

fun TodayQuestionResponse.toDomain(index: Int): Question = Question(
    id = questionId.toString(),
    number = index,
    title = question
)