package com.teampatch.feature.onboarding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.exception.FamilyRegistrationRequiredException
import com.teampatch.core.domain.model.Host
import com.teampatch.core.domain.model.Image
import com.teampatch.core.domain.model.InvitationMessage
import com.teampatch.core.domain.usecase.onboarding.LoginKakaoUseCase
import com.teampatch.core.domain.usecase.onboarding.RegisterFamilyUseCase
import com.teampatch.feature.onboarding.model.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    private val loginKakaoUseCase: LoginKakaoUseCase,
    private val registerFamilyUseCase: RegisterFamilyUseCase,
) : ViewModel() {

    private val _loginEvent: Channel<LoginEvent> = Channel()
    val loginEvent: Flow<LoginEvent> = _loginEvent.receiveAsFlow()

    fun loginKakao() = viewModelScope.launch {
        runCatching {
            loginKakaoUseCase()
        }.onSuccess {
            _loginEvent.send(LoginEvent.Success)
        }.onFailure { t ->
            if (t is FamilyRegistrationRequiredException) {
                _loginEvent.send(LoginEvent.FamilyRegistrationRequired)
                return@launch
            }
            _loginEvent.send(LoginEvent.Error(t))
            t.printStackTrace()
        }
    }

    // 가족 등록을 수행하는 함수
    fun registerFamily(
        host: Host,
        relation: String,
        name: String,
        profileImage: Image,
    ): LiveData<Result<InvitationMessage>> = liveData {
        emit(
            Result.runCatching {
                registerFamilyUseCase(
                    host = host,
                    relation = relation,
                    name = name,
                    profileImage = profileImage
                )
            }
        )
    }
}