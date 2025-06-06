package com.teampatch.core.network

import com.teampatch.core.domain.entities.TokenManager
import com.teampatch.core.network.di.NetworkSingletonModule
import com.teampatch.core.network.interceptor.TokenInterceptor
import java.util.regex.Pattern
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit

internal object TestRetrofit {

    private const val BASE_URL = "https://harmony-api2.azurewebsites.net"

    val tokenManager = object : TokenManager() {
        private var token: String = ""
        private val jsonContentType = "application/json; charset=utf-8".toMediaTypeOrNull()
        override val isTokenInvalidListener: Flow<Boolean>
            get() = flowOf(false)

        override fun getAccessToken(): String {
            if (token.isEmpty()) {
                val request = Request.Builder()
                    .url("$BASE_URL/user/signup")
                    .post(createSignUpRequestBody().toRequestBody(jsonContentType))
                    .build()

                val client = OkHttpClient.Builder().build()

                client.newCall(request).execute().use { response ->
                    token = extractToken(response.body!!.string())!!
                }
            }
            return token
        }

        override fun setAccessToken(token: String) {}
    }

    private fun createSignUpRequestBody(): String = """ { 
            "userId": "yeojeong@naver.com", 
            "nick": "윤여정", 
            "authProvider": "kakao", 
            "socialToken": "string", 
            "refreshToken": "string", 
            "socialTokenExpiredAt": 
            "2024-12-07T13:11:11.152Z" 
            } 
    """.trimIndent()

    private fun extractToken(jsonString: String): String? {
        val pattern = Pattern.compile("\"token\":\"([^\"]+)\"")
        val matcher = pattern.matcher(jsonString)
        return if (matcher.find()) matcher.group(1) else null
    }

    private val tokenInterceptor = TokenInterceptor(tokenManager)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(NetworkSingletonModule.provideMoshi())
        .callFactory(okHttpClient)
        .build()

    fun getRetrofit(): Retrofit = retrofit
}