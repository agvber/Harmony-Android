package com.teampatch.feature.onboarding.admission.model

internal sealed interface GroupAdmissionEvent {
    data object Success : GroupAdmissionEvent
    data object GroupCreateError : GroupAdmissionEvent
    data object GroupJoinError : GroupAdmissionEvent
    data object GroupInformationLoadError : GroupAdmissionEvent
}