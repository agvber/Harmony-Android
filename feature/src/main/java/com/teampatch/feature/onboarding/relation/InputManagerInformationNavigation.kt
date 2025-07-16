package com.teampatch.feature.onboarding.relation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object InputManagerInformationRoute

fun NavController.navigateToOnboardingInputManagerInformationScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = InputManagerInformationRoute,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addOnboardingInputManagerInformationScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: (relation: String, name: String) -> Unit,
) {
    composable<InputManagerInformationRoute> {
        InputManagerInformationScreen(
            onBackRequest = onBackRequest,
            onNextPageRequest = onNextPageRequest
        )
    }
}