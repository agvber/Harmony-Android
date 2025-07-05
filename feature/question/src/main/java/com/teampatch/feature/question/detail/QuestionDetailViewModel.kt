package com.teampatch.feature.question.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.common.DefaultSharingStarted
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.model.user.Role
import com.teampatch.core.domain.usecase.question.AddQuestionCommentUseCase
import com.teampatch.core.domain.usecase.question.DeleteQuestionCommentUseCase
import com.teampatch.core.domain.usecase.question.EditQuestionCommentUseCase
import com.teampatch.core.domain.usecase.question.GetQuestionCommentsUseCase
import com.teampatch.core.domain.usecase.question.GetQuestionDetailUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.question.detail.mapper.toPresentationModel
import com.teampatch.feature.question.detail.model.QuestionDetailEvent
import com.teampatch.feature.question.detail.model.QuestionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuestionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val getQuestionCommentsUseCase: GetQuestionCommentsUseCase,
    private val addCommentUseCase: AddQuestionCommentUseCase,
    private val editCommentUseCase: EditQuestionCommentUseCase,
    private val deleteCommentUseCase: DeleteQuestionCommentUseCase,
) : ViewModel() {

    private val questionDetailRoute: QuestionDetailRoute = savedStateHandle.toRoute()
    val questionId: String = questionDetailRoute.questionId

    private val _uiState: MutableStateFlow<QuestionDetailUiState> =
        MutableStateFlow(QuestionDetailUiState())
    val uiState: StateFlow<QuestionDetailUiState> = _uiState

    private val _event: Channel<QuestionDetailEvent> = Channel()
    val event: Flow<QuestionDetailEvent> = _event.receiveAsFlow()

    val comments: StateFlow<List<QuestionComment>> = flowExceptionSafety {
        getQuestionCommentsUseCase.invoke(questionId)
    }
        .catch { it.printStackTrace() }
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
            require(questionId.isNotEmpty())

            val user = getUserInfoUseCase.invoke().first()
            val questionDetail = getQuestionDetailUseCase(questionId)
            val post = questionDetail.toPresentationModel(user.role == Role.VIP)
            _uiState.value = QuestionDetailUiState(
                uid = user.uid,
                role = user.role,
                post = post,
                isLoading = false
            )
        }
            .onFailure { e ->
                _event.send(QuestionDetailEvent.LoadError(e))
                e.printStackTrace()
            }
    }

    fun updateQuestionAnswer(answer: String) {
        _uiState.update {
            it.copy(post = it.post.copy(content = answer))
        }
    }

    fun addComment(text: String) = viewModelScope.launch {
        runCatching { addCommentUseCase(questionId, text) }
            .onFailure { e ->
                _event.send(QuestionDetailEvent.AddCommentError(e))
                e.printStackTrace()
            }
    }

    fun editComment(id: String, text: String) = viewModelScope.launch {
        runCatching { editCommentUseCase(id, text) }
            .onFailure { e ->
                _event.send(QuestionDetailEvent.EditCommentError(e))
                e.printStackTrace()
            }
    }

    fun deleteComment(id: String) = viewModelScope.launch {
        runCatching { deleteCommentUseCase(id) }
            .onFailure { e ->
                _event.send(QuestionDetailEvent.DeleteCommentError(e))
                e.printStackTrace()
            }
    }
}