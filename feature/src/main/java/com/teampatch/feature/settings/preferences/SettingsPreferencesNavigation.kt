package com.teampatch.feature.settings.preferences

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingsPreferencesRoute

fun NavController.navigateToSettingsPreferencesScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(SettingsPreferencesRoute, navOptions, navigatorExtras)
}

/**
 * @param onTosClick Terms of service
 */

fun NavGraphBuilder.addSettingsPreferencesScreen(
    onBackRequest: () -> Unit,
    onExitAppRequest: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTosClick: () -> Unit,
) {
    composable<SettingsPreferencesRoute> {
        SettingsPreferencesRoute(
            onBackRequest = onBackRequest,
            onExitAppRequest = onExitAppRequest,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onTosClick = onTosClick
        )
    }
}