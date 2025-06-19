package com.teampatch.harmony

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object DailyRoute

fun NavController.navigateToDailyScreen(
    navOptions: NavOptions? = navOptions { launchSingleTop = true },
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(DailyRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addDailyScreen(
    dailyExpandPageRequest: () -> Unit,
) {
    composable<DailyRoute> {
        DailyRoute(
            dailyExpandPageRequest = dailyExpandPageRequest
        )
    }
}