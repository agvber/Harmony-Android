package com.teampatch.feature.onboarding.invitation.model

data class InputInvitationCodeUiState(
    val inviteCode: String = "",
    val isProgress: Boolean = false,
)