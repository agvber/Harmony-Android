package com.teampatch.feature.routine.certification

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.common.launchWithCatch
import com.teampatch.core.domain.usecase.routine.GetRoutineUseCase
import com.teampatch.feature.routine.certification.model.RoutineCertificationEvent
import com.teampatch.feature.routine.certification.model.RoutineCertificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
internal class RoutineCertificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRoutineUseCase: GetRoutineUseCase
) : ViewModel() {

    private val route: RoutineCertificationRoute = savedStateHandle.toRoute()

    private val _event: Channel<RoutineCertificationEvent> = Channel()
    val event: Flow<RoutineCertificationEvent> = _event.receiveAsFlow()

    private val _uiState: MutableStateFlow<RoutineCertificationUiState> =
        MutableStateFlow(RoutineCertificationUiState())
    val uiState: StateFlow<RoutineCertificationUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launchWithCatch(
            catch = { _event.send(RoutineCertificationEvent.InitLoadError) }
        ) {
            getRoutineUseCase.invoke(route.routineId)
        }
    }

    fun updateCertificationImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(certificationImage = uri)
    }
}