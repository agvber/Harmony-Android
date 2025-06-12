package com.teampatch.feature.onboarding.admission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.group.CreateGroupUseCase
import com.teampatch.core.domain.usecase.group.GetGroupInformationUseCase
import com.teampatch.core.domain.usecase.group.JoinFamilyGroupUseCase
import com.teampatch.feature.onboarding.admission.model.GroupAdmissionEvent
import com.teampatch.feature.onboarding.admission.model.GroupAdmissionUiState
import com.teampatch.feature.onboarding.admission.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GroupAdmissionViewModel @Inject constructor(
    private val joinFamilyGroupUseCase: JoinFamilyGroupUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val getGroupInformationUseCase: GetGroupInformationUseCase,
) : ViewModel() {

    private val _event: Channel<GroupAdmissionEvent> = Channel<GroupAdmissionEvent>()
    val event: Flow<GroupAdmissionEvent> = _event.receiveAsFlow()

    private val _uiState: MutableStateFlow<GroupAdmissionUiState> =
        MutableStateFlow(GroupAdmissionUiState.init())
    val uiState: StateFlow<GroupAdmissionUiState> = _uiState

    fun loadDemoGroupInformation(
        managerName: String,
        managerRelation: String
    ) = viewModelScope.launch {
        _uiState.value = GroupAdmissionUiState(
            manager = GroupAdmissionUiState.Manager(
                name = managerName,
                vipRelation = managerRelation,
                profileImageUri = null
            ),
            members = listOf(),
            memberSize = 1
        )
    }

    fun loadInvitedGroupInformation(inviteCode: String) = viewModelScope.launch {
        runCatching {
            val groupInformation = getGroupInformationUseCase.invoke(inviteCode)
            _uiState.value = groupInformation
                .copy(users = groupInformation.users.subList(0, 3))
                .toPresentation()
        }
            .onFailure {
                it.printStackTrace()
                _event.send(GroupAdmissionEvent.GroupInformationLoadError)
            }
    }

    fun createGroup(
        vipName: String,
        vipAlias: String,
        managerName: String,
        managerRelation: String,
        profileImageUri: Uri,
    ) = viewModelScope.launch {
        runCatching {
            createGroupUseCase(
                vipName = vipName,
                vipAlias = vipAlias,
                managerName = managerName,
                managerRelation = managerRelation,
                managerProfileImageUri = profileImageUri.toString()
            )
        }
            .onSuccess { _event.send(GroupAdmissionEvent.Success) }
            .onFailure {
                it.printStackTrace()
                _event.send(GroupAdmissionEvent.GroupCreateError)
            }
    }

    fun joinGroup(inviteCode: String) = viewModelScope.launch {
        runCatching { joinFamilyGroupUseCase(inviteCode) }
            .onSuccess { _event.send(GroupAdmissionEvent.Success) }
            .onFailure {
                it.printStackTrace()
                _event.send(GroupAdmissionEvent.GroupJoinError)
            }
    }

    override fun onCleared() {
        super.onCleared()
        _event.close()
    }
}