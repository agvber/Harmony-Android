package com.teampatch.feature.daily.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.SHARING_STARTED_TIME
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.usecase.daily.ToggleDailyRoutineStatusUseCase
import com.teampatch.core.domain.usecase.todo.GetTodosUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.daily.main.model.DailyMainEvent
import com.teampatch.feature.daily.main.model.DailyMainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DailyMainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val toggleDailyRoutineUseCase: ToggleDailyRoutineStatusUseCase,
    private val getTodosUseCase: GetTodosUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DailyMainUiState> =
        MutableStateFlow(DailyMainUiState())
    val uiState: StateFlow<DailyMainUiState> = _uiState

    private val _event: Channel<DailyMainEvent> = Channel<DailyMainEvent>()
    val event: Flow<DailyMainEvent> = _event.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val todos: StateFlow<List<CheckableData<Todo>>> =
        flowExceptionSafety {
            getTodosUseCase.invoke(uiState.value.now.toLocalDate())
        }
            .onEach { todo ->
                _uiState.update { it.copy(progress = todo.progress) }
            }
            .map { todos ->
                todos.todos.map {
                    CheckableData(it, mutableStateOf(it.isFinished))
                }
            }
            .catch {
                it.printStackTrace()
                _event.send(DailyMainEvent.LoadError(it))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SHARING_STARTED_TIME),
                initialValue = emptyList()
            )

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        runCatching {
            getUserInfoUseCase().collectLatest { user ->
                _uiState.update { it.copy(role = user.role, isLoading = false) }
            }
        }
            .onFailure {
                it.printStackTrace()
                _event.send(DailyMainEvent.LoadError(it))
            }
    }

    fun toggleRoutineFinished(routineId: String, checked: Boolean) = viewModelScope.launch {
        runCatching { toggleDailyRoutineUseCase(routineId, checked) }
            .onFailure {
                it.printStackTrace()
                _event.send(DailyMainEvent.RoutineStatusChangedError(it))
            }
    }
}