package com.teampatch.core.data.network.interceptor

import com.teampatch.core.data.network.annotation.AuthorizedRequest
import com.teampatch.core.domain.entities.TokenManager
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

internal class TokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        var request: Request = request()

        request.tag(Invocation::class.java)?.let { invocation ->
            invocation.method().annotations.forEach { annotation ->
                when (annotation) {
                    is AuthorizedRequest -> {
                        val accessToken = tokenManager.getAccessToken()
                        request = request.newBuilder()
                            .addHeader("Authorization", "Bearer $accessToken")
                            .build()
                    }
                }
            }
        }

        proceed(request)
    }
}