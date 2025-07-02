package com.teampatch.feature.memory.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.teampatch.core.common.flowErrorCatch
import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.core.domain.usecase.memory.GetMemoryCardsUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.memory.storage.model.MemoryCardSort
import com.teampatch.feature.memory.storage.model.MemoryStorageEvent
import com.teampatch.feature.memory.storage.model.MemoryStorageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MemoryStorageViewModel @Inject constructor(
    private val getMemoryCardsUseCase: GetMemoryCardsUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _memoryStorageEvent: Channel<MemoryStorageEvent> = Channel()
    val memoryStorageEvent: Flow<MemoryStorageEvent> = _memoryStorageEvent.receiveAsFlow()

    private val _uiState: MutableStateFlow<MemoryStorageUiState> = MutableStateFlow(MemoryStorageUiState())
    val uiState: StateFlow<MemoryStorageUiState> = _uiState

    fun loadData() = viewModelScope.launch {
        runCatching {
            getUserInfoUseCase.invoke().collectLatest { user ->
                _uiState.update { it.copy(userName = user.name, isLoading = false) }
            }
        }
            .onFailure { _memoryStorageEvent.send(MemoryStorageEvent.InitLoadError(it)) }
    }

    init {
        loadData()
    }

    val memoryCards: Flow<PagingData<MemoryCard>> = flowErrorCatch(
        block = { getMemoryCardsUseCase.invoke() },
        action = {
            it.printStackTrace()
            _memoryStorageEvent.send(MemoryStorageEvent.InitLoadError(it))
        }
    )

    fun updateMemoryCardSearchText(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    fun updateMemoryCardSortOption(sortOption: MemoryCardSort) {
        _uiState.update { it.copy(sortOption = sortOption) }
    }
}