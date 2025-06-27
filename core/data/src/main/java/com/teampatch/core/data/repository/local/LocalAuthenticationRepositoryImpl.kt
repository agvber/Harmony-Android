package com.teampatch.core.data.repository.local

import com.harmony.core.database.dao.UserDao
import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.data.service.authentication.KakaoLoginService
import com.teampatch.core.domain.entities.Group
import com.teampatch.core.domain.model.LoginResult
import com.teampatch.core.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class LocalAuthenticationRepositoryImpl @Inject constructor(
    private val kakaoLoginService: KakaoLoginService,
    private val userDao: UserDao,
    private val authenticationLocalDatasource: AuthenticationLocalDatasource
) : AuthenticationRepository {

    override suspend fun loginKakao(): LoginResult {
        val token = kakaoLoginService.login()
        authenticationLocalDatasource.setSocialLoginId(token.userId)
        val user = runCatching { userDao.getUserBySnsId(token.userId).first() }.getOrNull()
        val groupId = user?.groupId ?: return LoginResult(groupId = Group.IS_NOT_GROUP_CODE.toString())
        return LoginResult(groupId = groupId.toString())
    }

    override suspend fun logout() {
        authenticationLocalDatasource.deleteSocialLoginId()
    }
}