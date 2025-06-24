package com.teampatch.core.domain.usecase.daily

import com.teampatch.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyRoutineProgressUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {

    /**
     * 오늘 하루 루틴의 진행률을 반환합니다.
     * @return 오늘 하루 루틴의 진행률 (0.1 ~ 1)
     */

    suspend operator fun invoke(): Flow<Float> {
        return todoRepository.getDailyRoutineProgress()
    }
}