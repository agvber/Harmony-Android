package com.teampatch.harmony

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teampatch.core.common.flowErrorCatch
import com.teampatch.core.domain.usecase.authentication.IsLoginRequiredUseCase
import com.teampatch.core.domain.usecase.group.CheckIfGroupExistsUseCase
import com.teampatch.harmony.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginRequiredUseCase: IsLoginRequiredUseCase,
    private val checkIfGroupExistsUseCase: CheckIfGroupExistsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            flowErrorCatch(
                block = { isLoginRequiredUseCase() },
                action = {
                    _uiState.update { state ->
                        state.copy(
                            isLoginRequired = true,
                            isLoading = false
                        )
                    }
                    it.printStackTrace()
                }
            )
                .collect { isLoginRequired ->
                    _uiState.update { state ->
                        state.copy(
                            isLoginRequired = isLoginRequired,
                            isLoading = false,
                            isExistGroup = if (isLoginRequired) false else checkIfGroupExistsUseCase()
                        )
                    }
                }
        }
    }
}