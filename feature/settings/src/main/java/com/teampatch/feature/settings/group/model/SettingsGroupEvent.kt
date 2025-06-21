package com.teampatch.feature.settings.group.model

sealed interface SettingsGroupEvent {
    data class Invite(val inviteCode: String) : SettingsGroupEvent
    data class InviteError(val t: Throwable) : SettingsGroupEvent
    data class LoadError(val t: Throwable) : SettingsGroupEvent
}