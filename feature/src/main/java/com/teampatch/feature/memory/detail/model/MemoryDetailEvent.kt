package com.teampatch.feature.memory.detail.model

internal sealed interface MemoryDetailEvent {
    data object LoadError : MemoryDetailEvent
}