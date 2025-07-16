package com.teampatch.core.network.model.group.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupInviteRegenerateResponse(
    @Json(name = "message") val message: String,
    @Json(name = "newInviteCode") val newInviteCode: String,
)