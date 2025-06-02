package com.teampatch.feature.onboarding.invitation.model

internal sealed interface InputInvitationCodeEvent {
    data object Success : InputInvitationCodeEvent
    data class Error(val t: Throwable) : InputInvitationCodeEvent
}