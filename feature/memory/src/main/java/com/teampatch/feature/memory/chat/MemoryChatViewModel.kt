package com.teampatch.feature.memory.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.domain.usecase.memory.GetMemoryCardUseCase
import com.teampatch.core.domain.usecase.question.GetQuestionDetailUseCase
import com.teampatch.feature.memory.chat.model.MemoryChatEvent
import com.teampatch.feature.memory.chat.model.MemoryChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MemoryChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMemoryCardUseCase: GetMemoryCardUseCase,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase
) : ViewModel() {

    private val route: MemoryChatRoute = savedStateHandle.toRoute()

    private val _event: Channel<MemoryChatEvent> = Channel()
    val event: Flow<MemoryChatEvent> = _event.receiveAsFlow()

    private val _uiState: MutableStateFlow<MemoryChatUiState> =
        MutableStateFlow(MemoryChatUiState())
    val uiState: StateFlow<MemoryChatUiState> = _uiState.asStateFlow()

    private fun loadData() = viewModelScope.launch {
        runCatching {
            val memoryCard = getMemoryCardUseCase.invoke(route.memoryCardId)

            _uiState.update {
                it.copy(
                    id = memoryCard.id,
                    title = memoryCard.writerTitle,
                    question = TEST_QUESTION,
                    imageUrl = memoryCard.imageUrl,
                    answer = memoryCard.text,
                    date = memoryCard.dateTime.toLocalDate()
                )
            }
        }
            .onFailure {
                _event.send(MemoryChatEvent.LoadError)
            }
    }

    init {
        loadData()
    }

    override fun onCleared() {
        super.onCleared()
        _event.close()
    }

    companion object {
        private const val TEST_QUESTION: String = "ChatGPT Question"
    }
}