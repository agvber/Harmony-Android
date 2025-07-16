package com.teampatch.feature.question.answer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.domain.usecase.answer.AddAnswerUseCase
import com.teampatch.core.domain.usecase.answer.EditAnswerUseCase
import com.teampatch.core.domain.usecase.question.GetQuestionDetailUseCase
import com.teampatch.feature.question.answer.model.AnswerSideEffect
import com.teampatch.feature.question.answer.model.AnswerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AnswerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val addAnswerUseCase: AddAnswerUseCase,
    private val editAnswerUseCase: EditAnswerUseCase,
) : ViewModel() {

    private val answerRoute = savedStateHandle.toRoute<AnswerRoute>()

    var answerUiState = mutableStateOf(AnswerUiState())
        private set

    private val _sideEffect: Channel<AnswerSideEffect> = Channel()
    val sideEffect: Flow<AnswerSideEffect> = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        try {
            if (answerRoute.questionId.isEmpty()) return@launch

            val detail = getQuestionDetailUseCase(answerRoute.questionId)
            answerUiState.value = AnswerUiState(
                questionDetail = detail,
                isLoading = false
            )
        } catch (e: Exception) {
            _sideEffect.send(AnswerSideEffect.LoadError(e))
            e.printStackTrace()
        }
    }

    fun saveQuestionAnswer(answer: String) = viewModelScope.launch {
        try {
            if (answerUiState.value.questionDetail.content.isEmpty()) {
                addAnswerUseCase(answerRoute.questionId, answer)
                return@launch
            }
            editAnswerUseCase(answerRoute.questionId, answer)
        } catch (e: Exception) {
            _sideEffect.send(AnswerSideEffect.AddAnswerError(e))
            e.printStackTrace()
        }
    }
}