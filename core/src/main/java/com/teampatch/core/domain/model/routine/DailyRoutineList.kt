package com.teampatch.core.domain.model.routine

/**
 * [progress] : 해당 날짜의 진행 척도를 표현합니다. 0.1 - 1.0 범위
 */

data class DailyRoutineList(
    val dailyRoutines: List<DailyRoutine>,
    val totalItemCount: Int,
    val finishedItemCount: Int,
    val progress: Float
)