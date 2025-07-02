package com.teampatch.feature.home.model

import com.teampatch.core.domain.model.MemoryCard

sealed interface MemoryCardState {
    data object Wait : MemoryCardState
    data class Success(val data: MemoryCard) : MemoryCardState
    data class Error(val t: Throwable?) : MemoryCardState
}