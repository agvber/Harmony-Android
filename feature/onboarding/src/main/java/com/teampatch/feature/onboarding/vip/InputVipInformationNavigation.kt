package com.teampatch.feature.onboarding.vip

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingInputVipInformationRoute

fun NavController.navigateToOnboardingInputVipInformationScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate<OnboardingInputVipInformationRoute>(
        route = OnboardingInputVipInformationRoute,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addOnboardingInputVipInformationScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: (alias: String, name: String) -> Unit,
) {
    composable<OnboardingInputVipInformationRoute> {
        InputVipInformationScreenWithViewModel(
            onBackRequest = onBackRequest,
            onNextPageRequest = onNextPageRequest
        )
    }
}