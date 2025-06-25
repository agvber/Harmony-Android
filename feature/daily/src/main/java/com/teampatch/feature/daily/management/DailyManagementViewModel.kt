package com.teampatch.feature.daily.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.usecase.daily.GetDailyRoutineUseCase
import com.teampatch.feature.daily.management.model.DailyManagementEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
internal class DailyManagementViewModel @Inject constructor(
    private val getDailyRoutineUseCase: GetDailyRoutineUseCase,
) : ViewModel() {

    private val _event: Channel<DailyManagementEvent> = Channel<DailyManagementEvent>()
    val event: Flow<DailyManagementEvent> = _event.receiveAsFlow()

    val todos = flowExceptionSafety { getDailyRoutineUseCase.invoke() }
        .cachedIn(viewModelScope)
        .catch {
            it.printStackTrace()
            _event.send(DailyManagementEvent.LoadError(it))
        }

    fun deleteTodo(todoId: String) {

    }
}