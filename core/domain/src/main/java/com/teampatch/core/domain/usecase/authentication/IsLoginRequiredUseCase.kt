package com.teampatch.core.domain.usecase.authentication

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsLoginRequiredUseCase @Inject constructor() {

    @OptIn(ObsoleteCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> = flowOf(true)
}