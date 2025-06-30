package com.teampatch.core.domain.usecase.memory

import java.time.LocalDate
import javax.inject.Inject

class AddMemoryCardUseCase @Inject constructor() {

    suspend operator fun invoke(
        memories: String,
        date: LocalDate,
        imageUri: String
    ) {
    }
}