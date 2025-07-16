package com.teampatch.feature.memory.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.DefaultSharingStarted
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.model.FilterByItem
import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.core.domain.usecase.memory.GetMemoryCardsUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.memory.storage.model.MemoryCardSort
import com.teampatch.feature.memory.storage.model.MemoryStorageEvent
import com.teampatch.feature.memory.storage.model.MemoryStorageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class MemoryStorageViewModel @Inject constructor(
    private val getMemoryCardsUseCase: GetMemoryCardsUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _memoryStorageEvent: Channel<MemoryStorageEvent> = Channel()
    val memoryStorageEvent: Flow<MemoryStorageEvent> = _memoryStorageEvent.receiveAsFlow()

    private val _uiState: MutableStateFlow<MemoryStorageUiState> =
        MutableStateFlow(MemoryStorageUiState())
    val uiState: StateFlow<MemoryStorageUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val memoryCards: StateFlow<List<MemoryCard>> = uiState
        .distinctUntilChanged { old, new ->
            old.searchText == new.searchText && old.sortOption == new.sortOption
        }
        .debounce(SEARCH_DEBOUNCE_TIME)
        .flatMapLatest { uiState ->
            val filterByItem: FilterByItem = when (uiState.sortOption) {
                MemoryCardSort.OLDEST -> FilterByItem.OLDEST
                MemoryCardSort.LATEST -> FilterByItem.LATEST
                MemoryCardSort.NAME -> FilterByItem.ALPHABET
            }
            flowExceptionSafety {
                getMemoryCardsUseCase.invoke(uiState.searchText, filterByItem)
            }
        }
        .catch { it.printStackTrace() }
        .stateIn(
            scope = viewModelScope,
            started = DefaultSharingStarted,
            initialValue = emptyList()
        )

    init {
        loadData()
    }

    fun loadData() {
        flowExceptionSafety { getUserInfoUseCase.invoke() }
            .onEach {
                _uiState.update { uiState ->
                    uiState.copy(userName = it.name, isLoading = false)
                }
            }
            .catch {
                it.printStackTrace()
                _memoryStorageEvent.send(MemoryStorageEvent.InitLoadError(it))
            }
            .launchIn(viewModelScope)
    }

    fun updateMemoryCardSearchText(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    fun updateMemoryCardSortOption(sortOption: MemoryCardSort) {
        _uiState.update { it.copy(sortOption = sortOption) }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME: Long = 800L
    }
}