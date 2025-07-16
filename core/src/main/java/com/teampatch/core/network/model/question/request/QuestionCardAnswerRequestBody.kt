package com.teampatch.core.network.model.question.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionCardAnswerRequestBody(
    @Json(name = "answer") val answer: String,
)