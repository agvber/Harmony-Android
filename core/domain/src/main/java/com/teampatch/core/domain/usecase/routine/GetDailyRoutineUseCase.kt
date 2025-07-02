package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.model.routine.DailyRoutineList
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.time.LocalDate
import javax.inject.Inject

class GetDailyRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    operator fun invoke(date: LocalDate): Flow<DailyRoutineList> {
        return routineRepository.getDailyRoutine(date)
            .zip(routineRepository.getDailyRoutineProgress(date)) { dailyRoutines, taskProgress ->
                with(taskProgress) {
                    DailyRoutineList(
                        dailyRoutines = dailyRoutines,
                        totalItemCount = totalCount,
                        finishedItemCount = finishedCount,
                        progress = progress
                    )
                }
            }
    }
}