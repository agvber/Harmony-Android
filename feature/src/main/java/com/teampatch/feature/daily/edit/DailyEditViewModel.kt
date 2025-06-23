package com.teampatch.feature.daily.edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.daily.GetDailyManageUseCase
import com.teampatch.feature.daily.edit.model.DailyEditEvent
import com.teampatch.feature.daily.edit.model.DailyEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
internal class DailyEditViewModel @Inject constructor(
    private val getDailyManageUseCase: GetDailyManageUseCase,
) : ViewModel() {

    private val _dailyEditUiState = mutableStateOf(DailyEditUiState())
    val dailyEditUiState: State<DailyEditUiState> = _dailyEditUiState

    private val _event: Channel<DailyEditEvent> = Channel()
    val event: Flow<DailyEditEvent> = _event.receiveAsFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        runCatching {
            getDailyManageUseCase("someId") // 올바른 dailyId 사용
        }.onSuccess { dailyManage ->
            _dailyEditUiState.value = DailyEditUiState(
                dailyExpand = dailyManage,
                isLoading = false
            )
        }.onFailure {
            _event.send(DailyEditEvent.LoadError(it))
            it.printStackTrace()
        }
    }

    fun changeDailyContent(content: String) {
        _dailyEditUiState.value = _dailyEditUiState.value.copy(
            dailyExpand = _dailyEditUiState.value.dailyExpand.copy(content = content)
        )
    }

    fun toggleSelectedDay(day: DayOfWeek) {
        _dailyEditUiState.value = dailyEditUiState.value.copy(
            selectedDays = dailyEditUiState.value.selectedDays.toMutableSet().apply {
                if (contains(day)) remove(day) else add(day)
            }
        )
    }
}