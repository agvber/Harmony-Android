package com.teampatch.feature.home

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.teampatch.core.common.flowErrorCatch
import com.teampatch.core.common.toPagingData
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.model.Image
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.model.User
import com.teampatch.core.domain.usecase.daily.GetDailyRoutinesUseCase
import com.teampatch.core.domain.usecase.daily.ToggleDailyRoutineStatusUseCase
import com.teampatch.core.domain.usecase.memory.AddMemoryCardUseCase
import com.teampatch.core.domain.usecase.memory.GetLatestMemoryCardUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.home.model.HomeErrorHandler
import com.teampatch.feature.home.model.MemoryCardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getDailyRoutinesUseCase: GetDailyRoutinesUseCase,
    private val getLatestMemoryCardUseCase: GetLatestMemoryCardUseCase,
    private val toggleDailyRoutineStatusUseCase: ToggleDailyRoutineStatusUseCase,
    private val addMemoryCardUseCase: AddMemoryCardUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _errorHandler: MutableSharedFlow<HomeErrorHandler> = MutableSharedFlow()
    val errorHandler: SharedFlow<HomeErrorHandler> = _errorHandler.asSharedFlow()

    val user: StateFlow<User?> = flowErrorCatch({ getUserInfoUseCase() }) {
        it.printStackTrace()
        _errorHandler.emit(HomeErrorHandler.UserInfoLoadError(it))
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val dailyRoutine: Flow<PagingData<CheckableData<Todo>>> =
        flowErrorCatch(
            block = {
                getDailyRoutinesUseCase()
                    .map { pagingData ->
                        pagingData.map {
                            CheckableData(it, mutableStateOf(it.isFinished))
                        }
                    }
                    .cachedIn(viewModelScope)
            }
        ) {
            it.printStackTrace()
            emit(it.toPagingData())
        }

    val memoryCardUiState: StateFlow<MemoryCardUiState> =
        flowErrorCatch<MemoryCardUiState>(
            block = {
                getLatestMemoryCardUseCase()
                    .map { MemoryCardUiState.Success(it) }
            }
        ) {
            it.printStackTrace()
            emit(MemoryCardUiState.Error(it))
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MemoryCardUiState.Wait
            )

    fun changeDailyRoutine(routineId: String, checked: Boolean) = viewModelScope.launch {
        try {
            toggleDailyRoutineStatusUseCase(routineId, checked)
        } catch (e: Exception) {
            e.printStackTrace()
            _errorHandler.emit(HomeErrorHandler.ChangeDailyRoutineError(e))
        }
    }

    fun addMemoryCard(
        memories: String,
        dateTime: LocalDateTime,
        image: Uri,
    ) = viewModelScope.launch {
        try {
            addMemoryCardUseCase(
                memories = memories,
                dateTime = dateTime,
                image = Image.Uri(image.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
            _errorHandler.emit(HomeErrorHandler.MemoryCardAdditionError(e))
        }
    }
}