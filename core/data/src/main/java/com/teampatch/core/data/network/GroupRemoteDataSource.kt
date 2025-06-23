package com.teampatch.core.data.network

import com.teampatch.core.data.network.model.group.request.GroupCreationRequestBody
import com.teampatch.core.data.network.model.group.request.GroupJoinRequestBody
import com.teampatch.core.data.network.model.group.response.GroupCreationResponse
import com.teampatch.core.data.network.model.group.response.GroupInvitationCodeQueryResponse
import com.teampatch.core.data.network.model.group.response.GroupInviteRegenerateResponse
import com.teampatch.core.data.network.model.group.response.GroupJoinResponse
import com.teampatch.core.data.network.model.group.response.UserGroupListQueryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupRemoteDataSource {

    @POST("/group")
    suspend fun createGroup(
        @Body groupCreationRequestBody: GroupCreationRequestBody,
    ): GroupCreationResponse

    @POST("/group/join")
    suspend fun joinGroup(
        @Body groupJoinRequestBody: GroupJoinRequestBody,
    ): GroupJoinResponse

    @POST("/group/{groupId}/regenerate-invite")
    suspend fun regenerateGroupInviteCode(
        @Path("groupId") groupId: Int,
    ): GroupInviteRegenerateResponse

    @GET("/group/user/{userId}")
    suspend fun queryUserGroupList(
        @Path("userId") userId: String,
    ): UserGroupListQueryResponse

    @GET("/group/{groupId}/invite")
    suspend fun queryGroupInvitationCode(
        @Path("groupId") groupId: Int,
    ): GroupInvitationCodeQueryResponse
}