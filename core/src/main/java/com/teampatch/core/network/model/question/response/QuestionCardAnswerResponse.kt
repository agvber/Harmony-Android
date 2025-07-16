package com.teampatch.core.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionCardAnswerResponse(
    @Json(name = "questionId") val questionId: Int,
    @Json(name = "answer") val answer: String,
)