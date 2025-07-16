package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.repository.MemoryCardRepository
import java.io.InputStream
import javax.inject.Inject

class AddMemoryCardRecordUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,
) {

    suspend operator fun invoke(
        memoryCardId: String,
        question: String,
        audioFile: InputStream,
    ) {
        memoryCardRepository.addCommunication(memoryCardId, question, audioFile)
    }
}