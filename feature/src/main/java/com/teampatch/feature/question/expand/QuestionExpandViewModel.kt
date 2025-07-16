package com.teampatch.feature.question.expand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.DefaultSharingStarted
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.usecase.question.GetQuestionsUseCase
import com.teampatch.feature.question.expand.model.QuestionExpandEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class QuestionExpandViewModel @Inject constructor(
    getQuestionsUseCase: GetQuestionsUseCase,
) : ViewModel() {

    private val _event = Channel<QuestionExpandEvent>()
    val event = _event.receiveAsFlow()

    val questions: StateFlow<List<Question>> = flowExceptionSafety { getQuestionsUseCase() }
        .catch {
            _event.send(QuestionExpandEvent.LoadError(it))
            it.printStackTrace()
        }
        .stateIn(
            scope = viewModelScope,
            started = DefaultSharingStarted,
            initialValue = emptyList()
        )
}