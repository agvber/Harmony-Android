package com.teampatch.feature.daily.management.model

internal sealed interface DailyManagementEvent {
    data class LoadError(val t: Throwable) : DailyManagementEvent
    data object RoutineDeleteFailure : DailyManagementEvent
}