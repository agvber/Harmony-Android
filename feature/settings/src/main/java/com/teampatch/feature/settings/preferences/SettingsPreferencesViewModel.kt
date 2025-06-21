package com.teampatch.feature.settings.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.flowErrorCatch
import com.teampatch.core.domain.usecase.authentication.LogoutAppUseCase
import com.teampatch.core.domain.usecase.authentication.WithdrawAppUseCase
import com.teampatch.core.domain.usecase.authentication.WithdrawFamilyUseCase
import com.teampatch.core.domain.usecase.version.GetAppLatestVersionUseCase
import com.teampatch.feature.settings.preferences.model.SettingsPreferencesEvent
import com.teampatch.feature.settings.preferences.model.SettingsPreferencesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class SettingsPreferencesViewModel @Inject constructor(
    private val getAppLatestVersionUseCase: GetAppLatestVersionUseCase,
    private val logoutAppUseCase: LogoutAppUseCase,
    private val withdrawAppUseCase: WithdrawAppUseCase,
    private val withdrawFamilyUseCase: WithdrawFamilyUseCase,
) : ViewModel() {

    private val _event = Channel<SettingsPreferencesEvent>()
    val event: Flow<SettingsPreferencesEvent> = _event.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val settingsPreferencesUiState: StateFlow<SettingsPreferencesUiState> = flowErrorCatch(
        block = {
            getAppLatestVersionUseCase().mapLatest {
                SettingsPreferencesUiState(
                    isLatestVersion = it.isLatest,
                    installedVersion = it.installedVersionName,
                    isLoading = false
                )
            }
        }
    ) {
        it.printStackTrace()
        _event.send(SettingsPreferencesEvent.LoadError(it))
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsPreferencesUiState()
        )

    fun logout() = viewModelScope.launch {
        try {
            logoutAppUseCase()
            _event.send(SettingsPreferencesEvent.LogoutSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            _event.send(SettingsPreferencesEvent.LogoutError(e))
        }
    }

    fun withdrawFamilyGroup() = viewModelScope.launch {
        try {
            withdrawFamilyUseCase()
            _event.send(SettingsPreferencesEvent.WithdrawFamilyGroupSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            _event.send(SettingsPreferencesEvent.WithdrawFamilyGroupError(e))
        }
    }

    fun withdrawApp() = viewModelScope.launch {
        try {
            withdrawAppUseCase()
            _event.send(SettingsPreferencesEvent.WithdrawAppSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            _event.send(SettingsPreferencesEvent.WithdrawAppError(e))
        }
    }

    override fun onCleared() {
        super.onCleared()
        _event.close()
    }
}