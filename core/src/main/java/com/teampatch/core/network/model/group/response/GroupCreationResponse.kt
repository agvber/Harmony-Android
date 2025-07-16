package com.teampatch.core.network.model.group.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupCreationResponse(
    @Json(name = "groupId") val groupId: Int,
    @Json(name = "groupName") val groupName: String,
    @Json(name = "inviteUrl") val inviteUrl: String,
)