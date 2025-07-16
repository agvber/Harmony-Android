package com.teampatch.core.data.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

internal class AuthenticationLocalDatasourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AuthenticationLocalDatasource {

    override fun getSocialLoginId(): String {
        return sharedPreferences.getString(SOCIAL_LOGIN_ID, null)!!
    }

    override fun setSocialLoginId(socialLoginId: String) {
        sharedPreferences.edit(commit = true) {
            putString(SOCIAL_LOGIN_ID, socialLoginId)
        }
    }

    override fun deleteSocialLoginId() {
        sharedPreferences.edit { remove(SOCIAL_LOGIN_ID) }
    }

    private companion object AuthenticationUtils {
        const val SOCIAL_LOGIN_ID = "social_login_id"
    }
}