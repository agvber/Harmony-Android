package com.teampatch.feature.onboarding.invitation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingInputInvitationRoute

fun NavController.navigateToOnboardingInputInvitationScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    navigate(OnboardingInputInvitationRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingInputInvitationScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: () -> Unit,
) {
    composable<OnboardingInputInvitationRoute> {
        InputInvitationCodeWithViewModel(
            onBackRequest = onBackRequest,
            onNextPageRequest = onNextPageRequest
        )
    }
}