package com.teampatch.feature.onboarding.vip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.feature.onboarding.vip.model.InputVipInformationUiState
import com.teampatch.feature.onboarding.vip.model.VipAlias
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class InputVipInformationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(InputVipInformationUiState())
    val uiState: StateFlow<InputVipInformationUiState> = _uiState.asStateFlow()

    init {
        checkNicknameFormat()
    }

    private fun checkNicknameFormat() = viewModelScope.launch {
        uiState.collectLatest {
            _uiState.update {
                it.copy(isVipInformationValid = it.vipName.isNotBlank())
            }
        }
    }

    fun updateVipAlias(alias: VipAlias) {
        _uiState.update { it.copy(vipAlias = alias) }
    }

    fun updateVipName(name: String) {
        _uiState.update { it.copy(vipName = name) }
    }
}