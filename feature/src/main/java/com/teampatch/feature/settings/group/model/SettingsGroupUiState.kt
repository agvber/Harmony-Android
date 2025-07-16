package com.teampatch.feature.settings.group.model

import com.teampatch.core.domain.model.group.FamilyInfo
import com.teampatch.core.domain.model.user.User

data class SettingsGroupUiState(
    val user: User = User.createEmptyUser(),
    val familyInfo: List<FamilyInfo> = emptyList(),
    val isLoading: Boolean = true,
)