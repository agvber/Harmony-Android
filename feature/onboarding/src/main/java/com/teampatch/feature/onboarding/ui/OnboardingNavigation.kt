package com.teampatch.feature.onboarding.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * 1. OnboardingRoute
 * 2. 파라미터가 되는 OnboardingPermissionRoute, OnboardingStartRoute
 * 3. StartScreen의 파라미터가 되는 OnboardingMakeGroupRoute, OnboardingEnterInvitationCodeRoute
 */
@Serializable
data object OnboardingRoute

fun NavController.navigateToOnboardingScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingScreen(
    onHomeScreenRequest: () -> Unit,
    onPermissionNotificationRequest: () -> Unit,
    onStartScreenRequest: () -> Unit,
) {
    composable<OnboardingRoute> {
        OnboardingRoute(
            onHomeScreenRequest = onHomeScreenRequest,
            onPermissionNotificationRequest = onPermissionNotificationRequest,
            onStartScreenRequest = onStartScreenRequest
        )
    }
}

@Serializable
data object OnboardingPermissionRoute

fun NavController.navigateToPermissionNotificationScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingPermissionRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingPermissionNotificationScreen(
    onNextPageRequest: () -> Unit,
) {
    composable<OnboardingPermissionRoute> {
        OnboardingPermissionNotificationScreen(onNextPageRequest = onNextPageRequest)
    }
}

@Serializable
data object OnboardingStartRoute

fun NavController.navigateToStartScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingStartRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingStartScreen(
    onBackRequest: () -> Unit,
    onboardingMakeGroupRequest: () -> Unit,
    onboardingEnterScreenRequest: () -> Unit,
) {
    composable<OnboardingStartRoute> {
        OnboardingStartRoute(
            onBackRequest = onBackRequest,
            onMakeGroupRequest = onboardingMakeGroupRequest,
            onEnterScreenRequest = onboardingEnterScreenRequest
        )
    }
}