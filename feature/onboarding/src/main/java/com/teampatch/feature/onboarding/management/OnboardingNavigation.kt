package com.teampatch.feature.onboarding.management

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

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
        GroupManagementScreen(
            onBackRequest = onBackRequest,
            onboardingMakeGroupRequest = onboardingMakeGroupRequest,
            onboardingEnterScreenRequest = onboardingEnterScreenRequest
        )
    }
}