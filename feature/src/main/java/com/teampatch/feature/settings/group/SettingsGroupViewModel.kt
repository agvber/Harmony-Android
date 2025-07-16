package com.teampatch.feature.settings.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.family.GetFamilyInfoUseCase
import com.teampatch.core.domain.usecase.family.InviteFamilyUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.settings.group.model.SettingsGroupEvent
import com.teampatch.feature.settings.group.model.SettingsGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsGroupViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getFamilyInfoUseCase: GetFamilyInfoUseCase,
    private val inviteFamilyUseCase: InviteFamilyUseCase,
) : ViewModel() {

    private val _event: Channel<SettingsGroupEvent> = Channel()
    val event: Flow<SettingsGroupEvent> = _event.receiveAsFlow()

    private val _settingsGroupUiState = MutableStateFlow(SettingsGroupUiState())
    val familyInfoUiState = _settingsGroupUiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        combine(getUserInfoUseCase(), getFamilyInfoUseCase()) { user, familyInfo ->
            _settingsGroupUiState.value = SettingsGroupUiState(
                user = user,
                familyInfo = familyInfo,
                isLoading = false
            )
        }
            .catch {
                it.printStackTrace()
                _event.send(SettingsGroupEvent.LoadError(it))
            }
            .launchIn(viewModelScope)
    }

    fun inviteFamily() = viewModelScope.launch {
        try {
            _event.send(SettingsGroupEvent.Invite(inviteFamilyUseCase()))
        } catch (e: Exception) {
            e.printStackTrace()
            _event.send(SettingsGroupEvent.InviteError(e))
        }
    }

    override fun onCleared() {
        super.onCleared()
        _event.close()
    }
}