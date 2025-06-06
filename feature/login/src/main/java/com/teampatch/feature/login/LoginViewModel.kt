package com.teampatch.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.domain.exception.FamilyRegistrationRequiredException
import com.teampatch.core.domain.usecase.onboarding.LoginKakaoUseCase
import com.teampatch.feature.login.model.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginKakaoUseCase: LoginKakaoUseCase,
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
}