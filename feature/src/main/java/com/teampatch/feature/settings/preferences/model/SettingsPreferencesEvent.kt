package com.teampatch.feature.settings.preferences.model

sealed interface SettingsPreferencesEvent {

    data class LoadError(val t: Throwable) : SettingsPreferencesEvent

    data object LogoutSuccess : SettingsPreferencesEvent
    data class LogoutError(val t: Throwable) : SettingsPreferencesEvent

    data object WithdrawFamilyGroupSuccess : SettingsPreferencesEvent
    data class WithdrawFamilyGroupError(val t: Throwable) : SettingsPreferencesEvent

    data object WithdrawAppSuccess : SettingsPreferencesEvent
    data class WithdrawAppError(val t: Throwable) : SettingsPreferencesEvent
}