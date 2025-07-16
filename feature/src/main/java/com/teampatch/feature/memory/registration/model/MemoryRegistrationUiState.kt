package com.teampatch.feature.memory.registration.model

internal data class MemoryRegistrationUiState(
    val title: String = "",
    val imageUrl: String? = null,
    val questions: List<String> = emptyList(),
    val questionProgressIndex: Int = 0,
    val isLoading: Boolean = true,
)