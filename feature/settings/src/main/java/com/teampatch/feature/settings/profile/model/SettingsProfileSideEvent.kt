package com.teampatch.feature.settings.profile.model

sealed interface SettingsProfileSideEvent {
    data class LoadError(val t: Throwable) : SettingsProfileSideEvent
    data class SettingsProfileError(val t: Throwable) : SettingsProfileSideEvent
    data object SettingsProfileSuccess : SettingsProfileSideEvent
}