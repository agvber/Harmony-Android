package com.teampatch.core.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentEditResponse(
    @Json(name = "commentId") val commentId: Int,
    @Json(name = "content") val content: String,
)