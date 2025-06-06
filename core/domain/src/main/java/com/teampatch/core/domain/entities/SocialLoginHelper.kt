package com.teampatch.core.domain.entities

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialLoginHelper @Inject constructor() {

    private lateinit var socialUserId: String

    fun getSocialUserId(): String = socialUserId

    fun setSocialUserId(userId: String) {
        socialUserId = userId
    }
}