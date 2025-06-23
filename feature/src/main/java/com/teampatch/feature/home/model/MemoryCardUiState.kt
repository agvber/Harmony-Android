package com.teampatch.feature.home.model

import com.teampatch.core.domain.model.MemoryCard

sealed interface MemoryCardUiState {

    data object Wait : MemoryCardUiState

    data class Success(val data: MemoryCard) : MemoryCardUiState

    data class Error(val t: Throwable?) : MemoryCardUiState
}