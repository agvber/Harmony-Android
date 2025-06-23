package com.teampatch.feature.question.detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import com.teampatch.core.common.PagingDataHelper
import com.teampatch.core.domain.model.Role
import com.teampatch.core.domain.usecase.question.AddQuestionCommentUseCase
import com.teampatch.core.domain.usecase.question.DeleteQuestionCommentUseCase
import com.teampatch.core.domain.usecase.question.EditQuestionCommentUseCase
import com.teampatch.core.domain.usecase.question.GetQuestionDetailUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.question.detail.mapper.toPresentationModel
import com.teampatch.feature.question.detail.model.Comment
import com.teampatch.feature.question.detail.model.QuestionDetailSideEffect
import com.teampatch.feature.question.detail.model.QuestionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class QuestionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val addCommentUseCase: AddQuestionCommentUseCase,
    private val editCommentUseCase: EditQuestionCommentUseCase,
    private val deleteCommentUseCase: DeleteQuestionCommentUseCase,
) : ViewModel() {

    private val questionDetailRoute: QuestionDetailRoute = savedStateHandle.toRoute()
    val questionId: String = questionDetailRoute.questionId

    var uiState by mutableStateOf(QuestionDetailUiState())
        private set

    private val _sideEffect: Channel<QuestionDetailSideEffect> = Channel()
    val sideEffect: Flow<QuestionDetailSideEffect> = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        try {
            if (questionId.isEmpty()) {
                Log.d(TAG, "question_id is null")
                return@launch
            }

            val user = getUserInfoUseCase().first()
            val questionDetail = getQuestionDetailUseCase(questionId)

            val post = questionDetail.toPresentationModel(user.role == Role.VIP)
            val comments = questionDetail.comment.map { pagingData ->
                pagingData.map { it.toPresentationModel(user.name) }
            }
                .cachedIn(viewModelScope)
            uiState = QuestionDetailUiState(post, PagingDataHelper(comments), false)
        } catch (e: Exception) {
            _sideEffect.send(QuestionDetailSideEffect.LoadError(e))
            e.printStackTrace()
        }
    }

    fun updateQuestionAnswer(answer: String) {
        val post = uiState.post.copy(content = answer)
        uiState = uiState.copy(post = post)
    }

    fun addComment(text: String) = viewModelScope.launch {
        try {
            val questionComment = addCommentUseCase(questionId, text)
            val comment = questionComment.toPresentationModel("")
                .copy(hasWritePermission = true)
            uiState.comments.addItem(comment, true)
        } catch (e: Exception) {
            _sideEffect.send(QuestionDetailSideEffect.AddCommentError(e))
            e.printStackTrace()
        }
    }

    fun editComment(comment: Comment, text: String) = viewModelScope.launch {
        try {
            editCommentUseCase(comment.id, text)
            uiState.comments.editItem(comment, comment.copy(content = text))
        } catch (e: Exception) {
            _sideEffect.send(QuestionDetailSideEffect.EditCommentError(e))
            e.printStackTrace()
        }
    }

    fun deleteComment(comment: Comment) = viewModelScope.launch {
        try {
            deleteCommentUseCase(comment.id)
            uiState.comments.deleteItem(comment)
        } catch (e: Exception) {
            _sideEffect.send(QuestionDetailSideEffect.DeleteCommentError(e))
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "QuestionDetailViewModel"
    }
}