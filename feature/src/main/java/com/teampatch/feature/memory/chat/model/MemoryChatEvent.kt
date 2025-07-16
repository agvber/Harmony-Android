package com.teampatch.feature.memory.chat.model

internal sealed interface MemoryChatEvent {
    data object LoadError : MemoryChatEvent
}