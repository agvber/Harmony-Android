package com.teampatch.feature.onboarding.common

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class OnboardingCommonUiState(
    val vipName: String = "",
    val vipAlias: String = "",
    val managerName: String = "",
    val managerRelation: String = "",
    val profileImageUri: Uri = Uri.EMPTY,
    val action: OnboardingAction = OnboardingAction.INIT,
) : Parcelable {

    enum class OnboardingAction {
        JOIN,
        CREATE,
        INIT,
    }
}