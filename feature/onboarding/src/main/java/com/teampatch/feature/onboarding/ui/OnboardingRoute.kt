package com.teampatch.feature.onboarding.ui

import androidx.compose.runtime.Composable

@Composable
internal fun OnboardingRoute(
    onHomeScreenRequest: () -> Unit,
    onPermissionNotificationRequest: () -> Unit,
    onStartScreenRequest: () -> Unit,
) {
    OnboardingLoginScreen(
        onHomeScreenRequest = onHomeScreenRequest,
        onPermissionNotificationRequest = onPermissionNotificationRequest,
        onStartSpaceScreenRequest = onStartScreenRequest
    )
}

@Composable
internal fun OnboardingStartRoute(
    onBackRequest: () -> Unit,
    onMakeGroupRequest: () -> Unit,
    onEnterScreenRequest: () -> Unit,
) {
    OnboardingStartSpaceScreen(
        onBackRequest = onBackRequest,
        onboardingMakeGroupRequest = onMakeGroupRequest,
        onboardingEnterScreenRequest = onEnterScreenRequest
    )
}