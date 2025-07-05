package com.teampatch.feature.routine.management.model

internal sealed interface DailyManagementEvent {
    data class LoadError(val t: Throwable) : DailyManagementEvent
    data object RoutineDeleteFailure : DailyManagementEvent
}