package com.teampatch.feature.onboarding.vip.model

data class InputVipInformationUiState(
    val vipName: String = "",
    val vipAlias: VipAlias = VipAlias.GRAND_MOTHER,
    val isVipInformationValid: Boolean = false,
)