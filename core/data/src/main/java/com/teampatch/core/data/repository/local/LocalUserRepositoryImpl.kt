package com.teampatch.core.data.repository.local

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.harmony.core.database.dao.UserDao
import com.harmony.core.database.model.UserEntity
import com.teampatch.core.data.di.annotation.DispatchersContext
import com.teampatch.core.data.di.annotation.HarmonyDispatcher
import com.teampatch.core.data.mapper.MEMBER
import com.teampatch.core.data.mapper.VIP
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.data.utils.SOCIAL_LOGIN_ID
import com.teampatch.core.data.utils.getMediaStoreInfo
import com.teampatch.core.domain.model.Role
import com.teampatch.core.domain.model.User
import com.teampatch.core.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

internal class LocalUserRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userDao: UserDao,
    @ApplicationContext private val appContext: Context,
    @HarmonyDispatcher(DispatchersContext.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

    private val profileImageFolder: File by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        File("${appContext.dataDir.absolutePath}/profile_image")
            .also { it.mkdirs() }
    }

    override fun getUserInfo(): Flow<User> = userDao.getMyUserData().map {
        it.toDomain()
    }

    override suspend fun editProfile(name: String?, profileImageUri: String?) {
        val processedProfileImageUri: Uri? = profileImageUri?.toUri()?.let {
            withContext(ioDispatcher) {
                val contentResolver = appContext.contentResolver
                val mediaStoreInfo = contentResolver.getMediaStoreInfo(it)
                val mimeTypeMap = MimeTypeMap.getSingleton()
                val fileExtension = mimeTypeMap.getExtensionFromMimeType(mediaStoreInfo?.mimType)

                contentResolver.openInputStream(it)?.use { profileImageInputStream ->
                    val file =
                        File.createTempFile("profile_image", ".$fileExtension", profileImageFolder)
                    FileOutputStream(file).use {
                        it.write(profileImageInputStream.readBytes())
                    }
                    file.toUri()
                }
            }
        }

        val userEntity = userDao.getMyUserData().first()
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
        val snsId = sharedPreferences.getString(SOCIAL_LOGIN_ID, "") ?: ""
            .also { require(it.isNotBlank()) }

        val userEntity = UserEntity(
            uid = null,
            groupId = null,
            name = name,
            relation = relation,
            profileImageUri = role.name,
            role = when (role) {
                Role.VIP -> VIP
                Role.MEMBER -> MEMBER
            },
            snsId = snsId,
            isMe = true
        )
        userDao.insertUsers(userEntity)
    }
}