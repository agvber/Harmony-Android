package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.fake.FakeMemoryCard
import com.teampatch.core.domain.model.memory.MemoryCard
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLatestMemoryCardUseCase @Inject constructor() {

    operator fun invoke(): Flow<MemoryCard> = flow {
        delay(3000)
        emit(FakeMemoryCard().get()[0])
    }
}