package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.model.FilterByItem
import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.core.domain.repository.MemoryCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMemoryCardsUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,
) {

    operator fun invoke(
        keyword: String = "",
        filterBy: FilterByItem = FilterByItem.OLDEST,
    ): Flow<List<MemoryCard>> {
        return if (keyword.isBlank()) {
            memoryCardRepository.getAllMemoryCards()
        } else {
            memoryCardRepository.getMemoryCards(keyword)
        }
            .map {
                when (filterBy) {
                    FilterByItem.OLDEST -> it.sortedBy { it.dateTime }
                    FilterByItem.ALPHABET -> it.sortedBy { it.writerTitle }
                    FilterByItem.LATEST -> it.sortedByDescending { it.dateTime }
                }
            }
    }
}