package com.teampatch.feature.routine.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object DailyMainRoute

fun NavController.navigateToDailyMainScreen(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
        restoreState = true

        popUpTo(DailyMainRoute) {
            inclusive = true
        }
    },
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(DailyMainRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addDailyMainScreen(
    onCreationPageRequest: () -> Unit,
    onEditPageRequest: () -> Unit,
    onDetailPageRequest: (dailyId: String) -> Unit,
) {
    composable<DailyMainRoute> {
        DailyMainScreenWithViewModel(
            onCreationPageRequest = onCreationPageRequest,
            onEditPageRequest = onEditPageRequest,
            onDetailPageRequest = onDetailPageRequest
        )
    }
}