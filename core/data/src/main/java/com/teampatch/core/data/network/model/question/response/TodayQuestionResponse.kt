package com.teampatch.core.data.network.model.question.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodayQuestionResponse(
    @Json(name = "questionId") val questionId: Int,
    @Json(name = "groupId") val groupId: Int,
    @Json(name = "question") val question: String,
    @Json(name = "answer") val answer: String?,
)