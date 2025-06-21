package com.teampatch.harmony.model

data class MainUiState(
    val isLoginRequired: Boolean = false,
    val isLoading: Boolean = true,
    val isExistGroup: Boolean = true,
)