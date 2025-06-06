package com.teampatch.feature.onboarding.common

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.teampatch.core.common.ActivitySavedInstanceHelper
import com.teampatch.core.common.getCustomParcelableExtra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class OnboardingUiStateHelper : ActivitySavedInstanceHelper {

    private val _uiState: MutableStateFlow<OnboardingCommonUiState> =
        MutableStateFlow(OnboardingCommonUiState())
    val uiState: StateFlow<OnboardingCommonUiState> = _uiState.asStateFlow()

    fun updateAction(action: OnboardingCommonUiState.OnboardingAction) {
        _uiState.update { it.copy(action = action) }
    }

    fun updateVipInformation(
        vipName: String,
        vipAlias: String,
    ) {
        _uiState.update { it.copy(vipName = vipName, vipAlias = vipAlias) }
    }

    fun updateManagerInformation(
        managerName: String,
        managerRelation: String,
    ) {
        _uiState.update {
            it.copy(
                managerName = managerName,
                managerRelation = managerRelation
            )
        }
    }

    fun updateProfileImage(
        profileImageUri: Uri,
    ) {
        _uiState.update { it.copy(profileImageUri = profileImageUri) }
    }

    override fun saveState(bundle: Bundle) {
        bundle.putParcelable(KEY_COMMON_UI_STATE, _uiState.value)
    }

    override fun restoreState(bundle: Bundle) {
        if (!bundle.containsKey(KEY_COMMON_UI_STATE)) {
            Log.d("OnboardingUiStateHelper", "bundle does not contain key: $KEY_COMMON_UI_STATE")
            return
        }

        _uiState.value = bundle.getCustomParcelableExtra(
            name = KEY_COMMON_UI_STATE,
            clazz = OnboardingCommonUiState::class.java
        )
            .also { Log.d(TAG, "restore bundle parcelable: $it") }
            ?: return
    }

    companion object {
        const val TAG: String = "OnboardingUiStateHelper"

        private const val KEY_COMMON_UI_STATE = "common_ui_state"

        private var instance: OnboardingUiStateHelper? = null

        @Synchronized
        fun getInstance(): OnboardingUiStateHelper = instance ?: OnboardingUiStateHelper()
            .also { instance = it }
    }
}