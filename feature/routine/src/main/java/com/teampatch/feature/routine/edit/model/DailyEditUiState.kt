package com.teampatch.feature.routine.edit.model

import java.time.DayOfWeek
import java.time.LocalTime

internal data class DailyEditUiState(
    val title: String = "",
    val selectedDays: Set<DayOfWeek> = emptySet(),
    val time: LocalTime = LocalTime.now().withMinute(0),
    val dailyEditMode: DailyEditMode = DailyEditMode.ADD
) {

    fun isNextButtonEnabled(): Boolean = title.isNotBlank() && selectedDays.isNotEmpty()
}