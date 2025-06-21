package com.teampatch.feature.question.expand

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.question.GetQuestionsUseCase
import com.teampatch.feature.question.expand.model.QuestionExpandSideEffect
import com.teampatch.feature.question.expand.model.QuestionExpandUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class QuestionExpandViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
) : ViewModel() {

    var questionExpandUiState = mutableStateOf(QuestionExpandUiState())
        private set

    private val _sideEffect = Channel<QuestionExpandSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        val questions = getQuestionsUseCase(-1).catch {
            _sideEffect.send(QuestionExpandSideEffect.LoadError(it))
            it.printStackTrace()
        }
        questionExpandUiState.value = QuestionExpandUiState(
            question = questions,
            isLoading = false
        )
    }
}