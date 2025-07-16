package com.teampatch.core.network

import com.teampatch.core.network.model.FileUploadRequest
import com.teampatch.core.network.model.user.ProfileResponse
import com.teampatch.core.network.model.user.SignupOrLoginRequestBody
import com.teampatch.core.network.model.user.SignupOrLoginResponse

interface UserRemoteDataSource {

    suspend fun signupOrLogin(
        signupOrLoginRequestBody: SignupOrLoginRequestBody,
    ): SignupOrLoginResponse

    suspend fun logout()

    suspend fun getMyProfile(): ProfileResponse

    /**
     * @param username 사용자 이름
     * @param profileImage 프로필 이미지 파일 (10MB 이하, jpg/jpeg/png)
     */

    suspend fun editMyProfile(
        username: String?,
        profileImage: FileUploadRequest?,
    ): ProfileResponse
}