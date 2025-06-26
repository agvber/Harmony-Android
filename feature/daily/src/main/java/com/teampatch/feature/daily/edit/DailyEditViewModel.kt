package com.teampatch.feature.daily.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.common.launchWithCatch
import com.teampatch.core.domain.usecase.daily.AddDailyRoutineUseCase
import com.teampatch.core.domain.usecase.daily.EditDailyRoutineUseCase
import com.teampatch.core.domain.usecase.daily.GetDailyRoutineUseCase
import com.teampatch.feature.daily.edit.model.DailyEditEvent
import com.teampatch.feature.daily.edit.model.DailyEditMode
import com.teampatch.feature.daily.edit.model.DailyEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
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
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@HiltViewModel
internal class DailyEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDailyRoutineUseCase: GetDailyRoutineUseCase,
    private val addDailyRoutineUseCase: AddDailyRoutineUseCase,
    private val editDailyRoutineUseCase: EditDailyRoutineUseCase,
) : ViewModel() {

    private val route: DailyEditRoute = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(DailyEditUiState())
    val uiState: StateFlow<DailyEditUiState> = _uiState

    private val _event: Channel<DailyEditEvent> = Channel()
    val event: Flow<DailyEditEvent> = _event.receiveAsFlow()

    init {
        load()
    }

    private fun load() {
        if (route.dailyEditMode == DailyEditMode.ADD) return

        viewModelScope.launchWithCatch(
            catch = { _event.send(DailyEditEvent.LoadError(it)) }
        ) {
            with(getDailyRoutineUseCase(route.dailyId)) {
                _uiState.value = DailyEditUiState(
                    title = title,
                    dailyEditMode = DailyEditMode.EDIT,
                )
            }
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

    fun uploadDailyRoutine() = viewModelScope.launchWithCatch(
        catch = { _event.send(DailyEditEvent.AddDailyError(it)) }
    ) {
        with(uiState.value) {
            when (dailyEditMode) {
                DailyEditMode.ADD -> addDailyRoutineUseCase(title, selectedDays, time)
                DailyEditMode.EDIT -> editDailyRoutineUseCase(route.dailyId, title, selectedDays, time)
            }
        }
        _event.send(DailyEditEvent.DailyEditSuccess)
    }
}