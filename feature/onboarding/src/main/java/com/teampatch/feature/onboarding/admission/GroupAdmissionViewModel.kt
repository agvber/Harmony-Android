package com.teampatch.feature.onboarding.admission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.group.CreateFamilyGroupUseCase
import com.teampatch.core.domain.usecase.group.JoinFamilyGroupUseCase
import com.teampatch.feature.onboarding.admission.model.GroupAdmissionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GroupAdmissionViewModel @Inject constructor(
    private val createFamilyGroupUseCase: CreateFamilyGroupUseCase,
    private val joinFamilyGroupUseCase: JoinFamilyGroupUseCase,
) : ViewModel() {

    private val _event: Channel<GroupAdmissionEvent> = Channel<GroupAdmissionEvent>()
    val groupAdmissionEvent = _event.receiveAsFlow()

    private fun loadData() {
        // TODO: 해당 그룹의 정보를 가져오기
    }

    fun createGroup(
        vipName: String,
        vipAlias: String,
        managerName: String,
        managerRelation: String,
        profileImageUri: Uri,
    ) = viewModelScope.launch {
        runCatching { createFamilyGroupUseCase() }
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
}