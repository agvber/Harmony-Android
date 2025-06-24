package com.teampatch.feature.daily.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.usecase.daily.GetDailyRoutineUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.daily.main.model.DailyMainEvent
import com.teampatch.feature.daily.main.model.DailyMainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DailyMainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getDailyRoutineUseCase: GetDailyRoutineUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DailyMainUiState> =
        MutableStateFlow(DailyMainUiState())
    val uiState: StateFlow<DailyMainUiState> = _uiState

    private val _event: Channel<DailyMainEvent> = Channel<DailyMainEvent>()
    val event: Flow<DailyMainEvent> = _event.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val todos: Flow<PagingData<CheckableData<Todo>>> =
        flowExceptionSafety { getDailyRoutineUseCase() }
            .map { pagingData ->
                pagingData.map {
                    CheckableData(it, mutableStateOf(false))
                }
            }
            .catch {
                it.printStackTrace()
                _event.send(DailyMainEvent.LoadError(it))
            }

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        runCatching {
            getUserInfoUseCase.invoke().collectLatest {
                _uiState.value = _uiState.value.copy(role = it.role)
            }
        }
            .onFailure {
                it.printStackTrace()
                _event.send(DailyMainEvent.LoadError(it))
            }
    }
}