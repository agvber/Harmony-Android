package com.teampatch.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHomeScreen(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
        restoreState = true

        popUpTo(HomeRoute) {
            inclusive = true
        }
    },
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(HomeRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addHomeScreen(
    onUserPageRequest: () -> Unit,
    onMemoryCardCreationPageRequest: () -> Unit,
    onDailyRoutineRegisterPageRequest: () -> Unit,
    onDailyRoutineClick: (dailyRoutineId: String) -> Unit,
    onMemoryCardClick: (memoryCardId: String) -> Unit,
) {
    composable<HomeRoute> {
        HomeRoute(
            onUserPageRequest = onUserPageRequest,
            onMemoryCardCreationPageRequest = onMemoryCardCreationPageRequest,
            onDailyRoutineRegisterPageRequest = onDailyRoutineRegisterPageRequest,
            onDailyRoutineClick = onDailyRoutineClick,
            onMemoryCardClick = onMemoryCardClick
        )
    }
}