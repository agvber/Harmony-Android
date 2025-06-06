package com.teampatch.core.data.repository.local

import com.agvber.core.authentication.kakao.KakaoLoginService
import com.harmony.core.database.dao.UserDao
import com.teampatch.core.domain.entities.SocialLoginHelper
import com.teampatch.core.domain.entities.TokenManager
import com.teampatch.core.domain.model.LoginResult
import com.teampatch.core.domain.repository.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

internal class LocalAuthenticationRepositoryImpl @Inject constructor(
    private val kakaoLoginService: KakaoLoginService,
    private val tokenManager: TokenManager,
    private val userDao: UserDao,
    private val socialLoginHelper: SocialLoginHelper,
) : AuthenticationRepository {

    override suspend fun loginKakao(): LoginResult {
//        val token = kakaoLoginService.login()
//        socialLoginHelper.setSocialUserId(token.userId)
//        val user = userDao.getUserBySnsId(token.userId).firstOrNull()
        socialLoginHelper.setSocialUserId("12345678")
        val user = userDao.getUserBySnsId("12345678").firstOrNull()
        val groupId = user?.groupId ?: return LoginResult(groupId = EMPTY_GROUP_CODE)
        userDao.updateUser(user.copy(isMe = true))
        return LoginResult(groupId = groupId.toString())
    }

    override suspend fun logout() {
        val myUserData = userDao.getMyUserData().first()
        userDao.updateUser(myUserData.copy(isMe = false))
        tokenManager.setAccessToken("")
    }

    companion object {
        private const val EMPTY_GROUP_CODE: String = "-1"
    }
}