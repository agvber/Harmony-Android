package com.teampatch.core.network.impl

import com.teampatch.core.network.UserRemoteDataSource
import com.teampatch.core.network.model.FileUploadRequest
import com.teampatch.core.network.model.user.ProfileResponse
import com.teampatch.core.network.model.user.SignupOrLoginRequestBody
import com.teampatch.core.network.model.user.SignupOrLoginResponse
import com.teampatch.core.network.service.UserNetworkService
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userNetworkService: UserNetworkService,
) : UserRemoteDataSource {

    override suspend fun signupOrLogin(
        signupOrLoginRequestBody: SignupOrLoginRequestBody,
    ): SignupOrLoginResponse = userNetworkService.signupOrLogin(signupOrLoginRequestBody)

    override suspend fun logout() {
        userNetworkService.logout()
    }

    override suspend fun getMyProfile(): ProfileResponse = userNetworkService.getMyProfile()

    override suspend fun editMyProfile(username: String?, profileImage: FileUploadRequest?): ProfileResponse {
        val nickMultipart = username?.let {
            MultipartBody.Part.createFormData("nick", it)
        }

        val profileImageRequestBody: RequestBody? =
            profileImage?.fileContent?.let { profileImageInputStream ->
                withContext(Dispatchers.IO) {
                    object : RequestBody() {
                        override fun contentType(): MediaType? = profileImage.fileMediaType?.toMediaTypeOrNull()

                        override fun writeTo(sink: BufferedSink) {
                            sink.writeAll(profileImageInputStream.source())
                        }
                    }
                }
            }

        val profileImageMultipart = profileImageRequestBody?.let {
            MultipartBody.Part.createFormData(
                name = "profileImage",
                filename = profileImage.fileName,
                body = it
            )
        }

        return userNetworkService.editMyProfile(nickMultipart, profileImageMultipart)
    }
}