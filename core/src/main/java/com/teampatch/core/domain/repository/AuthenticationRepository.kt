package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.LoginResult

interface AuthenticationRepository {

    suspend fun loginKakao(): LoginResult

    suspend fun logout()
}