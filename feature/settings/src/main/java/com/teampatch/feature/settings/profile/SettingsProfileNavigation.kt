package com.teampatch.feature.settings.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingsProfileRoute

fun NavController.navigateToSettingsProfileScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(SettingsProfileRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addSettingsProfileScreen(
    onCompleteRequest: () -> Unit,
) {
    composable<SettingsProfileRoute> {
        SettingsProfileScreenWithViewModel(
            onCompleteRequest = onCompleteRequest
        )
    }
}