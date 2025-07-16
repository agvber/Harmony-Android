package com.teampatch.feature.memory.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.common.launchWithCatch
import com.teampatch.core.domain.usecase.memory.GetMemoryCardUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.memory.detail.model.MemoryDetailEvent
import com.teampatch.feature.memory.detail.model.MemoryDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class MemoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMemoryCardUseCase: GetMemoryCardUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val route: MemoryDetailRoute = savedStateHandle.toRoute()

    private val _event: Channel<MemoryDetailEvent> = Channel()
    val event: Flow<MemoryDetailEvent> = _event.receiveAsFlow()

    private val _uiState: MutableStateFlow<MemoryDetailUiState> =
        MutableStateFlow(MemoryDetailUiState())
    val uiState: StateFlow<MemoryDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        flowExceptionSafety { getUserInfoUseCase.invoke() }
            .onEach { user -> _uiState.update { it.copy(role = user.role) } }
            .catch {
                it.printStackTrace()
                _event.send(MemoryDetailEvent.LoadError)
            }
            .launchIn(viewModelScope)
        viewModelScope.launchWithCatch(
            catch = { _event.send(MemoryDetailEvent.LoadError) }
        ) {
            with(getMemoryCardUseCase(route.memoryCardId)) {
                _uiState.update {
                    it.copy(
                        id = id,
                        title = writerTitle,
                        description = text,
                        writtenDateTime = dateTime.toLocalDate(),
                        imageUrl = imageUrl,
                        tags = tags
                    )
                }
            }
        }
    }
}