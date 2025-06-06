package com.teampatch.core.network.interceptor

import com.teampatch.core.common.BuildConfig
import com.teampatch.core.domain.entities.TokenManager
import com.teampatch.core.network.UserRemoteDataSource
import com.teampatch.core.network.model.user.SignupOrLoginRequestBody
import com.teampatch.core.network.model.user.SignupOrLoginResponse
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

@Singleton
internal class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRemoteDataSource: dagger.Lazy<UserRemoteDataSource>,
) : Authenticator {

    private var attemptCount: AtomicInteger = AtomicInteger(0)

    override fun authenticate(route: Route?, response: Response): Request? {
        attemptCount.incrementAndGet()

        if (checkAuthenticateError(response)) {
            return null
        }

        if (IS_LOGGED_IN_DEBUG_BUILD_TYPE) {
            val devToken: String = getDevToken()
            tokenManager.setAccessToken(devToken)
            return response.request.newBuildToken(devToken)
        }

        tokenManager.setAccessToken("")
        return null
    }

    private fun checkAuthenticateError(response: Response): Boolean = response.code != 401 || attemptCount.get() > 10

    private fun getDevToken(): String {
        val signupOrLoginResponse: SignupOrLoginResponse =
            runBlocking { userRemoteDataSource.get().signupOrLogin(devSignupOrLoginRequestBody) }
        return signupOrLoginResponse.token
    }

    private fun Request.newBuildToken(token: String): Request = newBuilder()
        .removeHeader("Authorization")
        .addHeader("Authorization", "Bearer $token")
        .build()

    companion object {

        @Suppress("KotlinConstantConditions")
        private const val IS_LOGGED_IN_DEBUG_BUILD_TYPE: Boolean =
            BuildConfig.BUILD_TYPE == "loggedInDebug"

        private val devSignupOrLoginRequestBody by lazy {
            SignupOrLoginRequestBody(
                userId = "yeojeong@naver.com",
                nick = "윤여정",
                profile = "profile.png",
                authProvider = "kakao",
                socialToken = "kakao_social_token_example",
                refreshToken = "kakao_refresh_token_example",
                socialTokenExpiredAt = "2024-08-08 02:44:07"
            )
        }
    }
}