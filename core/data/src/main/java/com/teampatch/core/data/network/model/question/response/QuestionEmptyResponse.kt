package com.teampatch.core.data.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionEmptyResponse(
    @Json(name = "status") val status: String,
    @Json(name = "message") val message: String,
)