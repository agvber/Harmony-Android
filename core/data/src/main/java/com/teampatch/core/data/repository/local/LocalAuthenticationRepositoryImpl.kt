package com.teampatch.core.data.repository.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.agvber.core.authentication.kakao.KakaoLoginService
import com.harmony.core.database.dao.UserDao
import com.teampatch.core.data.utils.SOCIAL_LOGIN_ID
import com.teampatch.core.domain.model.LoginResult
import com.teampatch.core.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class LocalAuthenticationRepositoryImpl @Inject constructor(
    private val kakaoLoginService: KakaoLoginService,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : AuthenticationRepository {

    override suspend fun loginKakao(): LoginResult {
        val token = kakaoLoginService.login()
        sharedPreferences.edit { putString(SOCIAL_LOGIN_ID, token.userId) }
        val user = userDao.getUserBySnsId(token.userId).firstOrNull()
        val groupId = user?.groupId ?: return LoginResult(groupId = EMPTY_GROUP_CODE)
        userDao.updateUser(user.copy(isMe = true))
        return LoginResult(groupId = groupId.toString())
    }

    override suspend fun logout() {
        val myUserData = userDao.getMyUserData().first()
        userDao.updateUser(myUserData.copy(isMe = false))
        sharedPreferences.edit { remove(SOCIAL_LOGIN_ID) }
    }

    companion object {
        private const val EMPTY_GROUP_CODE: String = "-1"
    }
}