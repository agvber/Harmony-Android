package com.teampatch.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavController.navigateToLoginScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(LoginRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addLoginScreen(
    onHomeScreenRequest: () -> Unit,
    onPermissionNotificationRequest: () -> Unit,
    onStartScreenRequest: () -> Unit,
) {
    composable<LoginRoute> {
        LoginWithViewModelScreen(
            onHomeScreenRequest = onHomeScreenRequest,
            onPermissionNotificationRequest = onPermissionNotificationRequest,
            onStartSpaceScreenRequest = onStartScreenRequest
        )
    }
}