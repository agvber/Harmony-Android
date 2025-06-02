package com.teampatch.feature.onboarding.profile

internal sealed interface OnboardingProfileEditEvent {
    data object Success : OnboardingProfileEditEvent
    data object Failure : OnboardingProfileEditEvent
}