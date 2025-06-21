package com.teampatch.feature.daily.edit.model

import com.teampatch.core.domain.model.DailyManage
import java.time.DayOfWeek
import java.time.LocalDateTime

internal data class DailyEditUiState(
    val dailyExpand: DailyManage = DailyManage(
        id = "",
        number = 0,
        title = "",
        content = "",
        dateTime = LocalDateTime.now(),
        commentCount = 0
    ),
    val isLoading: Boolean = true,
    val selectedDays: Set<DayOfWeek> = emptySet(),
)