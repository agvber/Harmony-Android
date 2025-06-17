package com.teampatch.feature.memorystorage

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.teampatch.core.common.flowErrorCatch
import com.teampatch.core.domain.model.MemoryCard
import com.teampatch.core.domain.usecase.memory.GetMemoryCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
internal class MemoryStorageViewModel @Inject constructor(
    private val getMemoryCardsUseCase: GetMemoryCardsUseCase,
) : ViewModel() {

    private val _memoryStorageEvent: Channel<MemoryStorageEvent> = Channel()
    val memoryStorageEvent: Flow<MemoryStorageEvent> = _memoryStorageEvent.receiveAsFlow()

    val memoryCards: Flow<PagingData<MemoryCard>> = flowErrorCatch(
        block = { getMemoryCardsUseCase.invoke() },
        action = {
            it.printStackTrace()
            _memoryStorageEvent.send(MemoryStorageEvent.InitLoadError(it))
        }
    )
}