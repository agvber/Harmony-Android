package com.teampatch.feature.memory.creation.model

internal interface MemoryCreationEvent {
    data object MemoryCreationError : MemoryCreationEvent
    data object MemoryCreationSuccess : MemoryCreationEvent
}