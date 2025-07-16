package com.teampatch.core.network.model.group.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupCreationRequestBody(
    @Json(name = "userId") val userId: String,
    @Json(name = "name") val name: String,
    @Json(name = "deviceToken") val deviceToken: String,
)