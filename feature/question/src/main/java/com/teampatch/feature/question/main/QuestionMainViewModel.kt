package com.teampatch.feature.question.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.teampatch.core.domain.usecase.question.GetQuestionsUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.question.main.model.QuestionSideEffect
import com.teampatch.feature.question.main.model.QuestionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class QuestionMainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase,
) : ViewModel() {

    var questionUiState = mutableStateOf(QuestionUiState())
        private set

    private val _sideEffect = Channel<QuestionSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        try {
            val user = getUserInfoUseCase().first()
            val questions = getQuestionsUseCase()
                .catch {
                    it.printStackTrace()
                    _sideEffect.send(QuestionSideEffect.LoadError(it))
                }
                .cachedIn(viewModelScope)
            questionUiState.value =
                QuestionUiState(user = user, question = questions, isLoading = false)
        } catch (e: Exception) {
            _sideEffect.send(QuestionSideEffect.LoadError(e))
            e.printStackTrace()
        }
    }
}