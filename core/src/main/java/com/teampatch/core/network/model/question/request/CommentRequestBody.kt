package com.teampatch.core.network.model.question.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentRequestBody(
    @Json(name = "content") val content: String,
)