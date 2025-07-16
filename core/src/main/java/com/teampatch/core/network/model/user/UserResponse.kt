package com.teampatch.core.network.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "userId") val userId: String,
    @Json(name = "nick") val nick: String,
    @Json(name = "authProvider") val authProvider: String,
    @Json(name = "groupId") val groupId: Int,
    @Json(name = "permissionId") val permissionId: String?,
    @Json(name = "profileImage") val profileImage: String?,
)