package com.teampatch.feature.onboarding.profile

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingProfileSettingsRoute

fun NavController.navigateToOnboardingProfileSettingsScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingProfileSettingsRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingProfileSettingsScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: (uri: Uri) -> Unit,
) {
    composable<OnboardingProfileSettingsRoute> {
        ProfileSettingsScreen(onBackRequest = onBackRequest, onNextPageRequest = onNextPageRequest)
    }
}