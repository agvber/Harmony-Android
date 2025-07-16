package com.teampatch.feature.routine.management

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DailyManagementRoute

fun NavController.navigateToDailyManagementScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(DailyManagementRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addDailyManagementScreen(
    onBackRequest: () -> Unit,
    onEditPageRequest: (dailyId: String) -> Unit,
) {
    composable<DailyManagementRoute> {
        DailyManagementScreenWithViewModel(
            onBackRequest = onBackRequest,
            onEditPageRequest = onEditPageRequest,
        )
    }
}