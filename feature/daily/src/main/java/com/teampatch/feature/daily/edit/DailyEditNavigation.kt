package com.teampatch.feature.daily.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class DailyEditRoute(val dailyId: String)

fun NavController.navigateToDailyEditScreen(
    dailyId: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(DailyEditRoute(dailyId), navOptions, navigatorExtras)
}

fun NavGraphBuilder.addDailyEditScreen(
    onDismissRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
) {
    composable<DailyEditRoute> {
        DailyEditScreenWithViewModel(
            onDismissRequest = onDismissRequest,
            onCompleteRequest = onCompleteRequest
        )
    }
}