package com.teampatch.feature.routine.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.common.launchWithCatch
import com.teampatch.core.domain.model.routine.Routine
import com.teampatch.core.domain.usecase.routine.AddRoutineUseCase
import com.teampatch.core.domain.usecase.routine.EditRoutineUseCase
import com.teampatch.core.domain.usecase.routine.GetRoutineUseCase
import com.teampatch.feature.routine.edit.model.DailyEditEvent
import com.teampatch.feature.routine.edit.model.DailyEditMode
import com.teampatch.feature.routine.edit.model.DailyEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
internal class DailyEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRoutineUseCase: GetRoutineUseCase,
    private val addRoutineUseCase: AddRoutineUseCase,
    private val editRoutineUseCase: EditRoutineUseCase,
) : ViewModel() {

    private val route: DailyEditRoute = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(DailyEditUiState())
    val uiState: StateFlow<DailyEditUiState> = _uiState

    private val _event: Channel<DailyEditEvent> = Channel()
    val event: Flow<DailyEditEvent> = _event.receiveAsFlow()

    private val cacheRoutine = MutableStateFlow<Routine?>(null)

    init {
        load()
    }

    private fun load() {
        if (route.dailyEditMode == DailyEditMode.ADD) return

        viewModelScope.launchWithCatch(
            catch = { _event.send(DailyEditEvent.LoadError(it)) }
        ) {
            val routine: Routine = getRoutineUseCase.invoke(route.dailyId)
            _uiState.update {
                it.copy(
                    title = routine.name,
                    selectedDays = routine.daysOfWeekPeriod,
                    dailyEditMode = DailyEditMode.EDIT
                )
            }
            cacheRoutine.update { routine }
        }
    }

    fun changeTitleText(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun changeDayOfWeek(dayOfWeek: DayOfWeek) = _uiState.update {
        val isContains: Boolean = it.selectedDays.contains(dayOfWeek)
        val selectedDays = it.selectedDays.toMutableSet().apply {
            if (isContains) remove(dayOfWeek) else add(dayOfWeek)
        }
        it.copy(selectedDays = selectedDays)
    }

    fun changeTime(hour: Int, minute: Int) = _uiState.update { state ->
        runCatching { LocalTime.of(hour, minute) }
            .onFailure {
                it.printStackTrace()
                _event.trySend(DailyEditEvent.TimeFormatError)
            }
            .getOrNull()
            ?.let { state.copy(time = it) } ?: state
    }

    fun uploadDailyRoutine() = viewModelScope.launch {
        uiState.value.runCatching {
            when (dailyEditMode) {
                DailyEditMode.ADD -> addRoutineUseCase.invoke(
                    name = title,
                    daysOfWeekPeriod = selectedDays,
                    periodTime = time
                )

                DailyEditMode.EDIT -> editRoutineUseCase.invoke(
                    routineId = route.dailyId,
                    name = title,
                    daysOfWeekPeriod = selectedDays,
                    periodTime = time
                )
            }
        }
            .onSuccess {
                _event.send(DailyEditEvent.DailyEditSuccess)
            }
            .onFailure {
                it.printStackTrace()
            }
    }
}