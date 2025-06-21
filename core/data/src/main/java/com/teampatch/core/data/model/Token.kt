package com.teampatch.core.data.model

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class Token(
    val userId: String,
    val nickname: String,
    val profileUrl: String,
    val authProvider: String,
    val accessToken: String,
    val accessTokenExpiresAt: Date,
    val refreshToken: String,
    val refreshTokenExpiresAt: Date,
) : Parcelable