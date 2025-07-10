package com.teampatch.feature.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.DefaultSharingStarted
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.core.domain.usecase.memory.GetLatestMemoryCardUseCase
import com.teampatch.core.domain.usecase.routine.GetDailyRoutineUseCase
import com.teampatch.core.domain.usecase.routine.SetCheckableDailyRoutineUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.home.model.HomeEvent
import com.teampatch.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getLatestMemoryCardUseCase: GetLatestMemoryCardUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val setCheckableDailyRoutineUseCase: SetCheckableDailyRoutineUseCase,
    private val getDailyRoutineUseCase: GetDailyRoutineUseCase,
) : ViewModel() {

    private val _event: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    val dailyRoutine: StateFlow<List<CheckableData<DailyRoutine>>> =
        flowExceptionSafety { getDailyRoutineUseCase.invoke(uiState.value.now) }
            .map { routines ->
                routines.dailyRoutines.map { CheckableData(it, mutableStateOf(it.isFinished)) }
            }
            .catch {
                it.printStackTrace()
                _event.emit(HomeEvent.InitDataLoadError)
            }
            .stateIn(
                scope = viewModelScope,
                started = DefaultSharingStarted,
                initialValue = emptyList()
            )

    init {
        loadData()
    }

    private fun loadData() {
        flowExceptionSafety { getLatestMemoryCardUseCase.invoke() }
            .onEach {
                _uiState.update { uiState ->
                    uiState.copy(memoryCard = it)
                }
            }
            .catch { _event.emit(HomeEvent.MemoryCardReceiveError) }
            .launchIn(viewModelScope)

        flowExceptionSafety { getUserInfoUseCase.invoke() }
            .onEach {
                _uiState.update { uiState ->
                    uiState.copy(role = it.role)
                }
            }
            .catch { _event.emit(HomeEvent.InitDataLoadError) }
            .launchIn(viewModelScope)
    }

    fun changeDailyRoutine(routineId: String, checked: Boolean) = viewModelScope.launch {
        runCatching { setCheckableDailyRoutineUseCase(routineId, checked) }
            .onFailure {
                it.printStackTrace()
                _event.emit(HomeEvent.DailyRoutineUpdateError)
            }
    }
}