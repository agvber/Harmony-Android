package com.teampatch.feature.settings.profile.model

import com.teampatch.core.domain.model.Image
import com.teampatch.core.domain.model.user.Role

data class SettingsProfileUiState(
    val relation: String = "",
    val name: String = "",
    val profileImage: Image? = null,
    val role: Role = Role.MEMBER,
    val isLoading: Boolean = true,
)