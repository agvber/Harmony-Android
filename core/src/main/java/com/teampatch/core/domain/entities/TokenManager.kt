package com.teampatch.core.domain.entities

import kotlinx.coroutines.flow.Flow

abstract class TokenManager {

    abstract val isTokenInvalidListener: Flow<Boolean>

    abstract fun getAccessToken(): String
    abstract fun setAccessToken(token: String)

    companion object {
        const val ACCESS_TOKEN_KEY = "access_token"
    }
}