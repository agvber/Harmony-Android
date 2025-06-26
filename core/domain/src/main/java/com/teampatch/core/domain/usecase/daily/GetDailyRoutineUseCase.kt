package com.teampatch.core.domain.usecase.daily

import com.teampatch.core.domain.fake.FakeTodos
import com.teampatch.core.domain.model.Todo
import javax.inject.Inject

class GetDailyRoutineUseCase @Inject constructor() {

    suspend operator fun invoke(dailyId: String): Todo {
        return FakeTodos().get()[0]
    }
}