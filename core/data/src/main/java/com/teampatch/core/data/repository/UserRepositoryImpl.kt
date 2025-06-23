package com.teampatch.core.data.repository

import androidx.core.net.toUri
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.data.service.ImageUriCompressor
import com.teampatch.core.domain.model.Role
import com.teampatch.core.domain.model.User
import com.teampatch.core.domain.repository.UserRepository
import com.teampatch.core.data.network.UserRemoteDataSource
import com.teampatch.core.data.network.model.FileUploadRequest
import com.teampatch.core.data.network.model.user.ProfileResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.withContext

@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val imageUriCompressor: ImageUriCompressor,
) : UserRepository {

    private val user: MutableStateFlow<User?> = MutableStateFlow(null)

    override fun getUserInfo(): Flow<User> = user.map {
        it ?: user.updateAndGet {
            val userResponse = userRemoteDataSource.getMyProfile()
            userResponse.toDomain()
        }!!
    }

    override suspend fun editProfile(
        name: String?,
        profileImageUri: String?,
    ) = withContext(Dispatchers.IO) {
        val profileImage: FileUploadRequest? = profileImageUri?.let {
            imageUriCompressor.loadImage(it.toUri())

            withContext(Dispatchers.Default) {
                imageUriCompressor.compressImage(PROFILE_IMAGE_LIMIT_SIZE, qualityRange = IntRange(0, 1000))
            }.run {
                FileUploadRequest(
                    fileName = displayName,
                    fileMediaType = mimeType,
                    fileContent = bitmapInputStream
                )
            }
        }

        val profileResponse: ProfileResponse = userRemoteDataSource.editMyProfile(
            username = name,
            profileImage = profileImage
        )

        user.value = profileResponse.toDomain()
    }

    override suspend fun addUserProfile(
        name: String,
        relation: String,
        profileImageUrl: String?,
        role: Role,
    ) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val PROFILE_IMAGE_LIMIT_SIZE: Int = 10485760
    }
}