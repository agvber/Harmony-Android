package com.teampatch.feature.onboarding.admission.model

import androidx.core.net.toUri
import com.teampatch.core.domain.model.AdmissionGroupInformation

fun AdmissionGroupInformation.toPresentation(): GroupAdmissionUiState {
    return GroupAdmissionUiState(
        name = manager.name,
        vipRelation = manager.vipRelation,
        members = users.map { user ->
            GroupAdmissionUiState.Member(
                profileImageUri = user.profileImageUrl?.toUri()
            )
        }
    )
}