package com.teampatch.feature.daily.main.model

internal sealed interface DailyMainEvent {
    data class LoadError(val t: Throwable) : DailyMainEvent
    data class RoutineStatusChangedError(val t: Throwable) : DailyMainEvent
}