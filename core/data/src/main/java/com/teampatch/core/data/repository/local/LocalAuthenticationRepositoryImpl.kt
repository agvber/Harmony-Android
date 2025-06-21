package com.teampatch.core.data.repository.local

import com.harmony.core.database.dao.UserDao
import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.data.service.authentication.KakaoLoginService
import com.teampatch.core.domain.model.LoginResult
import com.teampatch.core.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class LocalAuthenticationRepositoryImpl @Inject constructor(
    private val kakaoLoginService: KakaoLoginService,
    private val userDao: UserDao,
    private val authenticationLocalDatasource: AuthenticationLocalDatasource
) : AuthenticationRepository {

    override suspend fun loginKakao(): LoginResult {
        val token = kakaoLoginService.login()
        authenticationLocalDatasource.setSocialLoginId(token.userId)
        val user = userDao.getUserBySnsId(token.userId).firstOrNull()
        val groupId = user?.groupId ?: return LoginResult(groupId = EMPTY_GROUP_CODE)
        return LoginResult(groupId = groupId.toString())
    }

    override suspend fun logout() {
        authenticationLocalDatasource.deleteSocialLoginId()
    }

    companion object {
        private const val EMPTY_GROUP_CODE: String = "-1"
    }
}