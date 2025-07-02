package com.teampatch.feature.home.model

internal sealed interface HomeEvent {

    data class ChangeDailyRoutineError(val t: Throwable) : HomeEvent

    data class MemoryCardAdditionError(val t: Throwable) : HomeEvent

    data class UserInfoLoadError(val t: Throwable) : HomeEvent
}