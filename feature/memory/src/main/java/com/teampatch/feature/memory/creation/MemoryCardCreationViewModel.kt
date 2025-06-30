package com.teampatch.feature.memory.creation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.memory.AddMemoryCardUseCase
import com.teampatch.feature.memory.creation.model.MemoryCreationEvent
import com.teampatch.feature.memory.creation.model.MemoryCreationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
internal class MemoryCardCreationViewModel @Inject constructor(
    private val addMemoryCardUseCase: AddMemoryCardUseCase,
) : ViewModel() {

    private val _event: Channel<MemoryCreationEvent> = Channel()
    val event = _event.receiveAsFlow()

    private val _uiState: MutableStateFlow<MemoryCreationUiState> =
        MutableStateFlow(MemoryCreationUiState())
    val uiState: StateFlow<MemoryCreationUiState> = _uiState.asStateFlow()

    fun addMemoryCard() = viewModelScope.launch {
        runCatching {
            with(uiState.value) {
                addMemoryCardUseCase(
                    memories = title,
                    date = date,
                    imageUri = imageUri!!.toString()
                )
            }
        }
            .onSuccess {
                _event.send(MemoryCreationEvent.MemoryCreationSuccess)
            }
            .onFailure {
                it.printStackTrace()
                _event.send(MemoryCreationEvent.MemoryCreationError)
            }
    }

    fun updateImage(uri: Uri) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun updateTitleText(titleText: String) {
        _uiState.update { it.copy(title = titleText) }
    }

    fun updateDate(millis: Long) {
        val instant = Instant.ofEpochMilli(millis)
        val date = LocalDate.ofInstant(instant, ZoneOffset.UTC)
        _uiState.update { it.copy(date = date) }
    }
}