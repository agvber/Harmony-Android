package com.teampatch.feature.onboarding.common.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class OnboardingCommonUiState(
    val action: OnboardingAction = OnboardingAction.INIT,
    val vipName: String = "",
    val vipAlias: String = "",
    val managerName: String = "",
    val managerRelation: String = "",
    val profileImageUri: Uri = Uri.EMPTY,
    val inviteCode: String = "",
) : Parcelable