package com.teampatch.core.network.model.group.response

import com.squareup.moshi.Json

data class GroupInvitationCodeQueryResponse(
    @Json(name = "inviteCode") val inviteCode: String,
    @Json(name = "groupName") val groupName: String,
)