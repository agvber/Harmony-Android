package com.teampatch.core.data.entity

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teampatch.core.common.BuildConfig
import com.teampatch.core.domain.entities.TokenManager
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class TokenManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : TokenManager() {

    override val isTokenInvalidListener: Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == ACCESS_TOKEN_KEY) {
                trySendBlocking(getAccessToken().isEmpty())
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
        .onStart {
            if (IS_LOGGED_IN_BUILD_TYPE) {
                emit(false)
                return@onStart
            }

            emit(getAccessToken().isEmpty())
        }

    override fun getAccessToken(): String = sharedPreferences.getString(ACCESS_TOKEN_KEY, "") ?: ""

    override fun setAccessToken(token: String) {
        sharedPreferences.edit {
            putString(ACCESS_TOKEN_KEY, token)
        }
    }

    companion object {
        @Suppress("KotlinConstantConditions")
        private const val IS_LOGGED_IN_BUILD_TYPE: Boolean = BuildConfig.BUILD_TYPE == "loggedInDebug"
    }
}