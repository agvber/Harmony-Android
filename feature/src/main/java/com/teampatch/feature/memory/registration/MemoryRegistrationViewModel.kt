package com.teampatch.feature.memory.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teampatch.core.domain.usecase.memory.AddMemoryCardAnswerUseCase
import com.teampatch.core.domain.usecase.memory.GetMemoryCardUseCase
import com.teampatch.feature.memory.registration.model.MemoryRegistrationEvent
import com.teampatch.feature.memory.registration.model.MemoryRegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MemoryRegistrationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMemoryCardUseCase: GetMemoryCardUseCase,
    private val addMemoryCardAnswerUseCase: AddMemoryCardAnswerUseCase,
) : ViewModel() {

    private val _sideEffect: Channel<MemoryRegistrationEvent> = Channel()
    val sideEffect: Flow<MemoryRegistrationEvent> = _sideEffect.receiveAsFlow()

    private val route: MemoryRegistrationRoute? = kotlin.runCatching {
        savedStateHandle.toRoute<MemoryRegistrationRoute>()
    }
        .onFailure {
            it.printStackTrace()
            _sideEffect.trySend(MemoryRegistrationEvent.LoadError)
        }
        .getOrNull()

    var uiState by mutableStateOf(MemoryRegistrationUiState())
        private set

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        try {
            val memoryCard = getMemoryCardUseCase(route!!.memoryCardId)
            uiState = uiState.copy(
                title = memoryCard.text,
                imageUrl = memoryCard.imageUrl,
                isLoading = false
            )
        } catch (e: Exception) {
            e.printStackTrace()
            _sideEffect.send(MemoryRegistrationEvent.LoadError)
        }
    }

    fun uploadMemoryCardAnswer(answer: String) = viewModelScope.launch {
        try {
            addMemoryCardAnswerUseCase(route!!.memoryCardId, answer)
        } catch (e: Exception) {
            e.printStackTrace()
            _sideEffect.send(MemoryRegistrationEvent.NetworkError)
        }
    }
}