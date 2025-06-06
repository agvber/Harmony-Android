package com.teampatch.feature.onboarding.permission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingPermissionRoute

fun NavController.navigateToOnboardingPermissionScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingPermissionRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingPermissionScreen(
    onNextPageRequest: () -> Unit,
) {
    composable<OnboardingPermissionRoute> {
        OnboardingPermissionScreen(onNextPageRequest = onNextPageRequest)
    }
}