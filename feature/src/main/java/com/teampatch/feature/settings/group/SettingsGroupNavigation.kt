package com.teampatch.feature.settings.group

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingsGroupRoute

fun NavController.navigateToSettingsGroupScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(SettingsGroupRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addSettingsGroupScreen(
    onBackRequest: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileEditClick: () -> Unit,
) {
    composable<SettingsGroupRoute> {
        SettingsGroupScreenWithViewModel(
            onBackRequest = onBackRequest,
            onSettingsClick = onSettingsClick,
            onProfileEditClick = onProfileEditClick
        )
    }
}