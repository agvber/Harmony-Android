package com.teampatch.core.domain.usecase.authentication

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsLoginRequiredUseCase @Inject constructor() {

    operator fun invoke(): Flow<Boolean> {
        return flowOf(false)
    }
}