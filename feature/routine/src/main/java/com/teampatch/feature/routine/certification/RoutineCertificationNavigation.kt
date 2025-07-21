package com.teampatch.feature.routine.certification

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class RoutineCertificationRoute(
    val routineId: String
)

fun NavController.navigateToRoutineCertificationScreen(
    routineId: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = RoutineCertificationRoute(routineId),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addRoutineCertificationScreen(
    onBackRequest: () -> Unit
) {
    composable<RoutineCertificationRoute> {
        RoutineCertificationScreenWithViewModel(
            onBackRequest = onBackRequest
        )
    }
}