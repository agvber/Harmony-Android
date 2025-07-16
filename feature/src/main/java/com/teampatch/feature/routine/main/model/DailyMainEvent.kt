package com.teampatch.feature.routine.main.model

internal sealed interface DailyMainEvent {
    data class LoadError(val t: Throwable) : DailyMainEvent
    data class RoutineStatusChangedError(val t: Throwable) : DailyMainEvent
}