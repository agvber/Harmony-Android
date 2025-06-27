package com.teampatch.feature.daily.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.model.Routine
import com.teampatch.core.domain.usecase.routine.DeleteRoutineUseCase
import com.teampatch.core.domain.usecase.routine.GetRoutinesUseCase
import com.teampatch.feature.daily.management.model.DailyManagementEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DailyManagementViewModel @Inject constructor(
    private val getRoutinesUseCase: GetRoutinesUseCase,
    private val deleteRoutineUseCase: DeleteRoutineUseCase,
) : ViewModel() {

    private val _event: Channel<DailyManagementEvent> = Channel<DailyManagementEvent>()
    val event: Flow<DailyManagementEvent> = _event.receiveAsFlow()

    val routines: StateFlow<List<Routine>> = flowExceptionSafety { getRoutinesUseCase() }
        .catch {
            it.printStackTrace()
            _event.send(DailyManagementEvent.LoadError(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteRoutine(routineId: String) = viewModelScope.launch {
        runCatching { deleteRoutineUseCase(routineId) }
            .onSuccess { _event.send(DailyManagementEvent.RoutineDeleteSuccess) }
            .onFailure { _event.send(DailyManagementEvent.RoutineDeleteFailure) }
    }
}