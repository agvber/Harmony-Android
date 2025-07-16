package com.teampatch.feature.home.model

internal sealed interface HomeEvent {
    data object DailyRoutineUpdateError : HomeEvent
    data object MemoryCardAdditionError : HomeEvent
    data object InitDataLoadError : HomeEvent
    data object MemoryCardReceiveError : HomeEvent
}