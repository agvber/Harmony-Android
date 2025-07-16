package com.teampatch.core.network.model.group.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupJoinResponse(
    @Json(name = "message") val message: String,
    @Json(name = "group") val group: Group,
    @Json(name = "permission") val permission: String,
) {
    data class Group(
        @Json(name = "groupId") val groupId: Int,
        @Json(name = "name") val name: String,
        @Json(name = "inviteUrl") val inviteUrl: String,
        @Json(name = "vipId") val vipId: String,
        @Json(name = "UserGroups") val userGroups: List<UserGroup>,
    ) {
        data class UserGroup(
            @Json(name = "ugId") val ugId: Int,
            @Json(name = "userId") val userId: String,
            @Json(name = "permissionId") val permissionId: String,
            @Json(name = "groupId") val groupId: Int,
            @Json(name = "alias") val alias: String,
            @Json(name = "deviceToken") val deviceToken: String,
            @Json(name = "User") val user: String,
        )
    }
}