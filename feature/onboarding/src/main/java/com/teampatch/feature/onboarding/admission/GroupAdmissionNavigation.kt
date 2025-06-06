package com.teampatch.feature.onboarding.admission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingGroupAdmissionRoute

fun NavController.navigateToOnboardingGroupAdmissionScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = OnboardingGroupAdmissionRoute,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addOnboardingGroupAdmissionScreen(
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
) {
    composable<OnboardingGroupAdmissionRoute> {
        GroupAdmissionWithViewModel(onBackRequest, onHomeRouteRequest)
    }
}