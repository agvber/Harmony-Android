package com.teampatch.core.data.repository

import com.teampatch.core.data.service.authentication.KakaoLoginService
import com.teampatch.core.data.utils.toServerDateFormat
import com.teampatch.core.domain.entities.TokenManager
import com.teampatch.core.domain.model.LoginResult
import com.teampatch.core.domain.repository.AuthenticationRepository
import com.teampatch.core.network.UserRemoteDataSource
import com.teampatch.core.network.model.user.SignupOrLoginRequestBody
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val kakaoLoginService: KakaoLoginService,
    private val tokenManager: TokenManager,
) : AuthenticationRepository {

    override suspend fun loginKakao(): LoginResult {
        val socialToken = kakaoLoginService.login()
        val body = SignupOrLoginRequestBody(
            userId = socialToken.userId,
            nick = socialToken.nickname,
            profile = socialToken.profileUrl,
            authProvider = socialToken.authProvider,
            socialToken = socialToken.accessToken,
            refreshToken = socialToken.refreshToken,
            socialTokenExpiredAt = socialToken.accessTokenExpiresAt.toServerDateFormat()
        )

        val signupOrLoginResponse = userRemoteDataSource.signupOrLogin(body)
        tokenManager.setAccessToken(signupOrLoginResponse.token)

        return LoginResult(groupId = signupOrLoginResponse.user.groupId)
    }

    override suspend fun logout() {
        userRemoteDataSource.logout()
        tokenManager.setAccessToken("")
    }
}