package com.teampatch.feature.memorystorage.model

sealed interface MemoryStorageEvent {
    data class InitLoadError(val t: Throwable) : MemoryStorageEvent
}