package com.teampatch.feature.settings.preferences.model

data class SettingsPreferencesUiState(
    val isLatestVersion: Boolean = true,
    val installedVersion: String = "",
    val isLoading: Boolean = true,
)