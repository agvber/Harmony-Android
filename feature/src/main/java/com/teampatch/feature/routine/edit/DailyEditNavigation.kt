package com.teampatch.feature.routine.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.teampatch.feature.routine.edit.model.DailyEditMode
import kotlinx.serialization.Serializable

@Serializable
data class DailyEditRoute(val dailyId: String, val dailyEditMode: DailyEditMode)

fun NavController.navigateToDailyEditScreen(
    dailyId: String,
    dailyEditMode: DailyEditMode,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val route = DailyEditRoute(dailyId, dailyEditMode)
    navigate(route, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addDailyEditScreen(
    onDismissRequest: () -> Unit,
) {
    composable<DailyEditRoute> {
        DailyEditScreenWithViewModel(
            onDismissRequest = onDismissRequest,
        )
    }
}