package com.teampatch.feature.onboarding

internal data class OnboardingCommonUiState(
    val vipName: String,
    val vipAlias: String,
    val managerName: String,
    val managerAlias: String
) {

    companion object {
        const val TAG = "OnboardingCommonUiState"
    }
}