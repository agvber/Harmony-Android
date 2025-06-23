package com.teampatch.core.data.repository.local

import android.net.Uri
import androidx.core.net.toUri
import com.teampatch.core.data.database.dao.UserDao
import com.teampatch.core.data.database.model.UserEntity
import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.data.di.annotation.DispatchersContext
import com.teampatch.core.data.di.annotation.HarmonyDispatcher
import com.teampatch.core.data.mapper.MEMBER
import com.teampatch.core.data.mapper.VIP
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.data.service.ImageCompressorService
import com.teampatch.core.data.service.ImageFormatTransferService
import com.teampatch.core.data.service.ImageSaverService
import com.teampatch.core.data.utils.FileFormat
import com.teampatch.core.domain.model.Role
import com.teampatch.core.domain.model.User
import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalUserRepositoryImpl @Inject constructor(
    private val authenticationLocalDatasource: AuthenticationLocalDatasource,
    private val userDao: UserDao,
    @HarmonyDispatcher(DispatchersContext.IO) private val ioDispatcher: CoroutineDispatcher,
    private val imageCompressorService: ImageCompressorService,
    private val imageFormatTransferService: ImageFormatTransferService,
    private val imageSaverService: ImageSaverService
) : UserRepository {

    override fun getUserInfo(): Flow<User> {
        val socialLoginId = authenticationLocalDatasource.getSocialLoginId()
        return userDao.getUserBySnsId(socialLoginId)
            .map { it.toDomain() }
    }

    override suspend fun editProfile(name: String?, profileImageUri: String?) {
        val processedProfileImageUri: Uri? =
            profileImageUri?.toUri()?.let { processProfileImage(it) }
        val socialLoginId = authenticationLocalDatasource.getSocialLoginId()
        val userEntity = userDao.getUserBySnsId(socialLoginId).first()
        val updateUserEntity = userEntity.copy(
            name = name ?: userEntity.name,
            profileImageUri = processedProfileImageUri?.toString() ?: userEntity.profileImageUri
        )
        userDao.updateUser(updateUserEntity)
    }

    override suspend fun addUserProfile(
        name: String,
        relation: String,
        profileImageUrl: String?,
        role: Role,
    ) {
        val snsId = authenticationLocalDatasource.getSocialLoginId()

        val userEntity = UserEntity(
            uid = null,
            groupId = null,
            name = name,
            relation = relation,
            profileImageUri = profileImageUrl?.let { processProfileImage(it.toUri()).toString() },
            role = when (role) {
                Role.VIP -> VIP
                Role.MEMBER -> MEMBER
            },
            snsId = snsId,
        )
        userDao.insertUsers(userEntity)
    }

    private suspend fun processProfileImage(contentResolverUri: Uri): Uri {
        return withContext(ioDispatcher) {
            imageFormatTransferService.getBitmapFormat(contentResolverUri)
                .let { imageCompressorService.compressImageWithTransferFormatJpeg(it) }
                .let { imageSaverService.saveProfileImage(it, FileFormat.JPEG) }
        }
    }
}