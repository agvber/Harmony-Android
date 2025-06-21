package com.teampatch.core.data.service.authentication

import com.teampatch.core.data.model.Token

internal interface SocialLoginService {
    suspend fun login(): Token
}