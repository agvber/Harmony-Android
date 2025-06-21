package com.teampatch.feature.daily.management

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.daily.GetDailyManageUseCase
import com.teampatch.feature.daily.management.model.DailyManagementEvent
import com.teampatch.feature.daily.management.model.DailyManagementUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DailyManagementViewModel @Inject constructor(
    private val getDailyManageUseCase: GetDailyManageUseCase,
) : ViewModel() {
    var dailyManagementUiState = mutableStateOf(DailyManagementUiState())
        private set

    private val _event = Channel<DailyManagementEvent>()
    val event = _event.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        runCatching {
            getDailyManageUseCase("someId") // 단일 데이터 반환
        }.onSuccess { daily ->
            dailyManagementUiState.value = DailyManagementUiState(
                dailyManage = daily,
                isLoading = false
            )
        }.onFailure {
            _event.send(DailyManagementEvent.LoadError(it))
            it.printStackTrace()
        }
    }
}