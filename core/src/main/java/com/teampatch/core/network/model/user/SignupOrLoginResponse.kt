package com.teampatch.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupOrLoginResponse(
    @Json(name = "message") val message: String,
    @Json(name = "user") val user: User,
    @Json(name = "token") val token: String,
) {
    data class User(
        @Json(name = "nick") val nick: String,
        @Json(name = "authProvider") val authProvider: String,
        @Json(name = "groupId") val groupId: String,
        @Json(name = "permissionId") val permissionId: String?,
    )
}