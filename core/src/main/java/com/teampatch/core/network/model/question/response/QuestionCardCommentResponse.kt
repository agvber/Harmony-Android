package com.teampatch.core.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionCardCommentResponse(
    @Json(name = "commentId") val commentId: Int,
    @Json(name = "questionId") val questionId: Int,
    @Json(name = "authorId") val authorId: String,
    @Json(name = "content") val content: String,
    @Json(name = "createdAt") val createdAt: String,
)