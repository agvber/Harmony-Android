package com.teampatch.feature.memorystorage.model

internal data class MemoryStorageUiState(
    val userName: String = "",
    val sortOption: MemoryCardSort = MemoryCardSort.OLDEST,
    val searchText: String = "",
    val isLoading: Boolean = true
)