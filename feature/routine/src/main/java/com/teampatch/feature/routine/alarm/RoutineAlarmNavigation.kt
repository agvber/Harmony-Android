package com.teampatch.feature.routine.alarm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializable
data class RoutineAlarmRoute(
    val routineTitle: String,
    val routinePeriodTime: String,
) {
    constructor(routineTitle: String, routinePeriodTime: LocalTime) : this(
        routineTitle,
        routinePeriodTime.format(DATE_TIME_FORMATTER)
    )

    fun getRoutinePeriodTime(): LocalTime {
        return LocalTime.parse(routinePeriodTime, DATE_TIME_FORMATTER)
    }

    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    }
}

fun NavController.navigateToRoutineAlarmScreen(
    routineTitle: String,
    routinePeriodTime: LocalTime,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = RoutineAlarmRoute(routineTitle, routinePeriodTime),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addRoutineAlarmScreen(
    onLaterTaskRequest: () -> Unit,
    onHistoryPageRequest: () -> Unit,
) {
    composable<RoutineAlarmRoute> { navBackStackEntry ->
        val route: RoutineAlarmRoute = navBackStackEntry.toRoute()

        RoutineAlarmScreen(
            onLaterTaskRequest = onLaterTaskRequest,
            onHistoryPageRequest = onHistoryPageRequest,
            title = route.routineTitle,
            time = route.getRoutinePeriodTime(),
        )
    }
}