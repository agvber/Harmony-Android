package com.teampatch.core.data.repository.local

import android.net.Uri
import androidx.core.net.toUri
import com.teampatch.core.common.DefaultSharingStarted
import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.data.di.annotation.DispatchersContext
import com.teampatch.core.data.di.annotation.HarmonyDispatcher
import com.teampatch.core.data.mapper.MEMBER
import com.teampatch.core.data.mapper.VIP
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.data.service.image.ImageCompressorService
import com.teampatch.core.data.service.image.ImageFormatTransferService
import com.teampatch.core.data.service.image.ImageSaverService
import com.teampatch.core.data.utils.FileFormat
import com.teampatch.core.database.dao.UserDao
import com.teampatch.core.database.model.UserEntity
import com.teampatch.core.domain.model.user.Role
import com.teampatch.core.domain.model.user.User
import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalUserRepositoryImpl @Inject constructor(
    private val authenticationLocalDatasource: AuthenticationLocalDatasource,
    private val userDao: UserDao,
    @HarmonyDispatcher(DispatchersContext.IO) private val ioDispatcher: CoroutineDispatcher,
    coroutineScope: CoroutineScope,
    private val imageCompressorService: ImageCompressorService,
    private val imageFormatTransferService: ImageFormatTransferService,
    private val imageSaverService: ImageSaverService
) : UserRepository {

    private val socialLoginId: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val user: StateFlow<User?> = socialLoginId.flatMapLatest {
        if (it.isEmpty()) return@flatMapLatest flowOf(null)
        userDao.getUserBySnsId(it).map { it.toDomain() }
    }
        .stateIn(
            scope = coroutineScope,
            started = DefaultSharingStarted,
            initialValue = null
        )

    override fun getUserInfo(): Flow<User> {
        if (socialLoginId.value.isEmpty()) {
            socialLoginId.value = authenticationLocalDatasource.getSocialLoginId()
        }
        return user.mapNotNull { it }
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