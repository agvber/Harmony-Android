package com.teampatch.feature.settings.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.model.Image
import com.teampatch.core.domain.usecase.profile.EditProfileUseCase
import com.teampatch.core.domain.usecase.user.GetUserInfoUseCase
import com.teampatch.feature.settings.profile.model.SettingsProfileSideEvent
import com.teampatch.feature.settings.profile.model.SettingsProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsProfileViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _event: Channel<SettingsProfileSideEvent> = Channel()
    val event: Flow<SettingsProfileSideEvent> = _event.receiveAsFlow()

    private val _uiState = MutableStateFlow(SettingsProfileUiState())
    val profileEditUiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        try {
            val user = getUserInfoUseCase().first()
            _uiState.value = SettingsProfileUiState(
                relation = user.relation,
                name = user.name,
                profileImage = user.profileImageUrl?.let { Image.Url(it) },
                role = user.role,
                isLoading = false
            )
        } catch (e: Exception) {
            _event.send(SettingsProfileSideEvent.LoadError(e))
        }
    }

    fun updateRelation(relation: String) {
        _uiState.update { it.copy(relation = relation) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateProfileImage(uri: Uri) {
        _uiState.update { it.copy(profileImage = Image.Uri(uri.toString())) }
    }

    fun editProfile() = viewModelScope.launch {
        try {
            val uiState = profileEditUiState.value
            editProfileUseCase(uiState.name, (uiState.profileImage as? Image.Uri)?.uri)
            _event.send(SettingsProfileSideEvent.SettingsProfileSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            _event.send(SettingsProfileSideEvent.SettingsProfileError(e))
        }
    }

    override fun onCleared() {
        super.onCleared()
        _event.close()
    }
}