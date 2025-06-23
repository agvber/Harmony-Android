package com.teampatch.core.data.network.model.group.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserGroupListQueryResponse(
    @Json(name = "groups") val groups: List<Groups>,
) {
    data class Groups(
        @Json(name = "groupId") val groupId: Int,
        @Json(name = "name") val name: String,
        @Json(name = "permissionId") val permissionId: String?,
        @Json(name = "myAlias") val myAlias: String?,
        @Json(name = "members") val members: List<Members>,
    ) {
        data class Members(
            @Json(name = "userId") val userId: String,
            @Json(name = "nick") val nick: String,
            @Json(name = "profile") val profile: String?,
            @Json(name = "alias") val alias: String?,
            @Json(name = "permissionId") val permissionId: String,
        )
    }
}