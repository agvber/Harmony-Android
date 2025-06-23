package com.teampatch.feature.daily.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.usecase.daily.GetDailyRoutineUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.daily.main.model.DailyMainEvent
import com.teampatch.feature.daily.main.model.DailyMainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    private val _event = Channel<DailyMainEvent>()
    val event = _event.receiveAsFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        try {
            val user = getUserInfoUseCase().first()
            val todo = getDailyRoutineUseCase().map { pagingData ->
                pagingData.map {
                    CheckableData(it, mutableStateOf(it.isFinished))
                }
            }
            _uiState.value = DailyMainUiState(user = user, daily = todo, isLoading = false)
        } catch (e: Exception) {
            _event.send(DailyMainEvent.LoadError(e))
            e.printStackTrace()
        }
    }
}