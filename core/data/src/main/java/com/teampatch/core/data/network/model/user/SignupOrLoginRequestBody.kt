package com.teampatch.core.data.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupOrLoginRequestBody(
    @Json(name = "userId") val userId: String,
    @Json(name = "nick") val nick: String,
    @Json(name = "profile") val profile: String,
    @Json(name = "authProvider") val authProvider: String,
    @Json(name = "socialToken") val socialToken: String,
    @Json(name = "refreshToken") val refreshToken: String,
    @Json(name = "socialTokenExpiredAt") val socialTokenExpiredAt: String,
)