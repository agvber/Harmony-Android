package com.teampatch.core.data.datasource

internal interface AuthenticationLocalDatasource {
    fun getSocialLoginId(): String
    fun setSocialLoginId(socialLoginId: String)
    fun deleteSocialLoginId()
}