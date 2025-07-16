package com.teampatch.core.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionProvideResponse(
    @Json(name = "pqid") val pQid: Int,
    @Json(name = "question") val question: String,
)