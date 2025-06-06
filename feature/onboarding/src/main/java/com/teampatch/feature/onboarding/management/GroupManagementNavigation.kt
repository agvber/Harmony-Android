package com.teampatch.feature.onboarding.management

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingGroupManagementRoute

fun NavController.navigateToOnboardingGroupManagementScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingGroupManagementRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingGroupManagementScreen(
    onBackRequest: () -> Unit,
    onGroupCreateRequest: () -> Unit,
    onGroupJoinRequest: () -> Unit,
) {
    composable<OnboardingGroupManagementRoute> {
        GroupManagementScreen(
            onBackRequest = onBackRequest,
            onGroupCreateRequest = onGroupCreateRequest,
            onGroupJoinRequest = onGroupJoinRequest
        )
    }
}