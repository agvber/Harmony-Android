package com.teampatch.feature.onboarding.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

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
        LoginRoute(
            onHomeScreenRequest = onHomeScreenRequest,
            onPermissionNotificationRequest = onPermissionNotificationRequest,
            onStartSpaceScreenRequest = onStartScreenRequest
        )
    }
}