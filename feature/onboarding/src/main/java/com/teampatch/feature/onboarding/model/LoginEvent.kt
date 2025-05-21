package com.teampatch.feature.onboarding.model

internal sealed interface LoginEvent {
    data object Success : LoginEvent
    data object FamilyRegistrationRequired : LoginEvent
    data class Error(val t: Throwable) : LoginEvent
}