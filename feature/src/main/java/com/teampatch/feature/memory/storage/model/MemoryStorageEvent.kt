package com.teampatch.feature.memory.storage.model

sealed interface MemoryStorageEvent {
    data class InitLoadError(val t: Throwable) : MemoryStorageEvent
}