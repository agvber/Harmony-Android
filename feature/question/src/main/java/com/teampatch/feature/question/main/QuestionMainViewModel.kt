package com.teampatch.feature.question.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.DefaultSharingStarted
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.usecase.question.GetQuestionsUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.question.main.model.QuestionMainEvent
import com.teampatch.feature.question.main.model.QuestionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuestionMainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    getQuestionsUseCase: GetQuestionsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState

    private val _event = Channel<QuestionMainEvent>()
    val event = _event.receiveAsFlow()

    val questions: StateFlow<List<Question>> = flowExceptionSafety {
        getQuestionsUseCase(3)
    }
        .catch {
            it.printStackTrace()
            _event.send(QuestionMainEvent.LoadError(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = DefaultSharingStarted,
            initialValue = emptyList()
        )

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        runCatching {
            getUserInfoUseCase.invoke().collectLatest { user ->
                _uiState.update { it.copy(role = user.role, isLoading = false) }
            }
        }
            .onFailure { e ->
                _event.send(QuestionMainEvent.LoadError(e))
                e.printStackTrace()
            }
    }
}