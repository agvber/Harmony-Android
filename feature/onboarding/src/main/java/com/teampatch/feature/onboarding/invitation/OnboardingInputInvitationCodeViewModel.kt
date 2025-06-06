package com.teampatch.feature.onboarding.invitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.group.JoinFamilyGroupUseCase
import com.teampatch.feature.onboarding.invitation.model.InputInvitationCodeEvent
import com.teampatch.feature.onboarding.invitation.model.InputInvitationCodeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class OnboardingInputInvitationCodeViewModel @Inject constructor(
    private val joinFamilyGroupUseCase: JoinFamilyGroupUseCase,
) : ViewModel() {

    private val _event: Channel<InputInvitationCodeEvent> = Channel()
    val event = _event.receiveAsFlow()

    private val _uiState = MutableStateFlow(InputInvitationCodeUiState())
    val uiState: StateFlow<InputInvitationCodeUiState> = _uiState.asStateFlow()

    fun updateInviteCode(inviteCode: String) {
        _uiState.update { it.copy(inviteCode = inviteCode) }
    }

    fun confirmInviteCode() = viewModelScope.launch {
        try {
            joinFamilyGroupUseCase(uiState.value.inviteCode)
        } catch (e: Exception) {
            e.printStackTrace()
            _event.send(InputInvitationCodeEvent.Error(e))
        }
    }
}