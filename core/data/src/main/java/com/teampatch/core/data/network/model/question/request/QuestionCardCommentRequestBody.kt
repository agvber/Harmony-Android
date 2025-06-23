package com.teampatch.core.data.network.model.question.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionCardCommentRequestBody(
    @Json(name = "questionId") val questionId: Int,
    @Json(name = "groupId") val groupId: Int,
    @Json(name = "authorId") val authorId: String,
    @Json(name = "content") val content: String,
)