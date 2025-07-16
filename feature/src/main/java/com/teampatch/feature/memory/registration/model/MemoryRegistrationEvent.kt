package com.teampatch.feature.memory.registration.model

internal sealed interface MemoryRegistrationEvent {

    data object LoadError : MemoryRegistrationEvent
    data object RecordingError : MemoryRegistrationEvent
    data object RecordingPermissionDeniedError : MemoryRegistrationEvent
    data object NetworkError : MemoryRegistrationEvent
}