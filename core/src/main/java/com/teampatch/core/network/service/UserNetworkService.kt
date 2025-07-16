package com.teampatch.core.network.service

import com.teampatch.core.network.annotation.AuthorizedRequest
import com.teampatch.core.network.model.user.ProfileResponse
import com.teampatch.core.network.model.user.SignupOrLoginRequestBody
import com.teampatch.core.network.model.user.SignupOrLoginResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

internal interface UserNetworkService {

    @POST("/user/signup")
    suspend fun signupOrLogin(
        @Body body: SignupOrLoginRequestBody,
    ): SignupOrLoginResponse

    @AuthorizedRequest
    @POST("/user/logout")
    suspend fun logout()

    @AuthorizedRequest
    @GET("/user/profile")
    suspend fun getMyProfile(): ProfileResponse

    @AuthorizedRequest
    @Multipart
    @PATCH("/user/profile")
    suspend fun editMyProfile(
        @Part nick: MultipartBody.Part?,
        @Part profileImage: MultipartBody.Part?,
    ): ProfileResponse
}