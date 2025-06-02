package com.teampatch.feature.onboarding.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.usecase.profile.EditProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OnboardingProfileEditViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    private val _event: Channel<OnboardingProfileEditEvent> = Channel()
    val event = _event.receiveAsFlow()

    fun uploadProfileImage(profileImageUri: String) = viewModelScope.launch {
        try {
            editProfileUseCase(null, profileImageUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}