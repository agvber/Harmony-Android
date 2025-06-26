package com.teampatch.feature.daily.edit.model

internal sealed interface DailyEditEvent {
    data class LoadError(val t: Throwable) : DailyEditEvent
    data class AddDailyError(val t: Throwable) : DailyEditEvent
    data object TimeFormatError : DailyEditEvent
    data object DailyEditSuccess : DailyEditEvent
}