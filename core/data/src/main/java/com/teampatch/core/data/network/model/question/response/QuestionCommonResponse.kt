package com.teampatch.core.data.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionCommonResponse<T>(
    @Json(name = "status") val status: String,
    @Json(name = "message") val message: String,
    @Json(name = "data") val data: T,
)