package com.teampatch.core.data.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentCreateResponse(
    @Json(name = "commentId") val commentId: Int,
    @Json(name = "questionId") val questionId: Int,
    @Json(name = "authorId") val authorId: String,
    @Json(name = "content") val content: String,
)