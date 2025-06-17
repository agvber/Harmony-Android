package com.teampatch.feature.memorystorage

sealed interface MemoryStorageEvent {
    data class InitLoadError(val t: Throwable) : MemoryStorageEvent
}