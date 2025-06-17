package com.teampatch.core.data.repository.local

import android.content.SharedPreferences
import android.net.Uri
import androidx.core.net.toUri
import com.harmony.core.database.dao.GroupDao
import com.harmony.core.database.dao.UserDao
import com.harmony.core.database.model.GroupEntity
import com.harmony.core.database.model.UserEntity
import com.teampatch.core.data.mapper.MEMBER
import com.teampatch.core.data.mapper.roleStringMapper
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.data.service.ImageCompressorService
import com.teampatch.core.data.service.ImageFormatTransferService
import com.teampatch.core.data.service.ImageSaverService
import com.teampatch.core.data.utils.FileFormat
import com.teampatch.core.data.utils.SOCIAL_LOGIN_ID
import com.teampatch.core.domain.model.AdmissionGroupInformation
import com.teampatch.core.domain.model.FamilyInfo
import com.teampatch.core.domain.model.InvitedGroup
import com.teampatch.core.domain.model.UserGroup
import com.teampatch.core.domain.repository.GroupManagementRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject
import kotlin.random.Random

internal class LocalGroupManagementRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences,
    private val imageFormatTransferService: ImageFormatTransferService,
    private val imageCompressorService: ImageCompressorService,
    private val imageSaverService: ImageSaverService
) : GroupManagementRepository {

    override suspend fun createFamilyGroup(): String {
        val socialLoginId = sharedPreferences.getString(SOCIAL_LOGIN_ID, "")!!
        val myUserData = userDao.getUserBySnsId(socialLoginId).first()
        val inviteCode = Random.nextLong(10000, 99999).toString()
        val groupEntity = GroupEntity(
            id = null,
            vipUid = null,
            managerUid = myUserData.uid!!,
            inviteCode = inviteCode
        )
        val groupInsertedIds = groupDao.insertGroups(groupEntity)
        userDao.updateUser(myUserData.copy(groupId = groupInsertedIds[0]))
        return inviteCode
    }

    override suspend fun generateInviteCode(): String {
        val socialLoginId = sharedPreferences.getString(SOCIAL_LOGIN_ID, "")!!
        val myUserData = userDao.getUserBySnsId(socialLoginId).first()
        val groupEntity = groupDao.queryGroupById(myUserData.groupId!!)
        return groupEntity.first().inviteCode
    }

    override suspend fun joinFamilyGroup(inviteCode: String): InvitedGroup {
        val group = groupDao.queryGroupByInviteCode(inviteCode).first()
        val groupMembers = userDao.getUserByGroupId(group.id!!.toLong()).first()
        val managerUserData = userDao.getUserById(group.managerUid!!).first()

        return InvitedGroup(
            groupId = group.id?.toInt()!!,
            groupManagerInfo = InvitedGroup.GroupManagerInfo(managerUserData.name),
            users = groupMembers.map {
                InvitedGroup.User(it.profileImageUri)
            }
        )
    }

    override suspend fun getUserGroupList(uid: String): List<UserGroup> {
        val userEntity = userDao.getUserById(uid.toLong()).first()
        val userGroupIds = userEntity.groupId
            ?.let { listOf(it) }
            ?: emptyList()

        return userGroupIds.map { groupId ->
            groupDao.queryGroupById(groupId).map { groupEntity ->
                UserGroup(
                    groupId = groupId.toString(),
                    name = "",
                    members = userDao.getUserByGroupId(groupId).map { userEntities ->
                        userEntities.map {
                            FamilyInfo(
                                title = it.relation,
                                name = it.name,
                                isManager = groupEntity.managerUid == it.uid,
                                role = roleStringMapper(it.role),
                                profileImageUrl = it.profileImageUri
                            )
                        }
                    }
                        .first()
                )
            }
                .first()
        }
    }

    override suspend fun queryGroupInvitationCode(): String {
        TODO("Not yet implemented")
    }

    override suspend fun createGroup(
        vipName: String,
        vipAlias: String,
        managerName: String,
        managerRelation: String,
        managerProfileImageUri: String?
    ) {
        val snsId = sharedPreferences.getString(SOCIAL_LOGIN_ID, "") ?: ""
            .also { require(it.isNotBlank()) }

        val userEntity = UserEntity(
            uid = null,
            groupId = null,
            name = managerName,
            relation = managerRelation,
            profileImageUri = managerProfileImageUri,
            role = MEMBER,
            snsId = snsId,
        )
        userDao.insertUsers(userEntity)
        val mangerInformation = userDao.getUserBySnsId(snsId).first()
        val groupEntity = GroupEntity(
            id = null,
            vipUid = null,
            managerUid = mangerInformation.uid,
            inviteCode = Random.nextLong(10000, 99999).toString()
        )
        groupDao.insertGroups(groupEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun joinGroup(
        memberName: String,
        vipRelation: String,
        memberProfileImageUri: String?,
        inviteCode: String
    ) {
        val snsId = sharedPreferences.getString(SOCIAL_LOGIN_ID, "") ?: ""
            .also { require(it.isNotBlank()) }

        val processedImageUri: Uri? = memberProfileImageUri?.let {
            imageFormatTransferService.getBitmapFormat(it.toUri())
                .let { imageCompressorService.compressImageWithTransferFormatJpeg(it) }
                .use { imageSaverService.saveProfileImage(it, FileFormat.JPEG) }
        }

        groupDao.queryGroupByInviteCode(inviteCode).mapLatest { invitedGroup ->
            val userEntity = UserEntity(
                uid = null,
                groupId = invitedGroup.id,
                name = memberName,
                relation = vipRelation,
                profileImageUri = processedImageUri?.toString(),
                role = MEMBER,
                snsId = snsId
            )
            userDao.insertUsers(userEntity)
        }
            .first()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun searchGroup(inviteCode: String): AdmissionGroupInformation {
        return groupDao.queryGroupByInviteCode(inviteCode).flatMapLatest { groupEntity ->
            userDao.getUserByGroupId(groupEntity.id!!).zip(
                userDao.getUserById(groupEntity.managerUid!!)
            ) { groupMembers, manger ->
                groupEntity.toDomain(
                    managerInformation = manger.toDomain(),
                    groupMembers = groupMembers.mapNotNull {
                        if (it.uid == manger.uid) return@mapNotNull null
                        it.toDomain()
                    }
                )
            }
        }
            .first()
    }

    override suspend fun isGroupExist(inviteCode: String): Boolean {
        return groupDao.queryGroupByInviteCode(inviteCode).firstOrNull() != null
    }
}