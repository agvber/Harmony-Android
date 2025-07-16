package com.teampatch.core.domain.model.group

import com.teampatch.core.domain.model.user.Role

data class FamilyInfo(
    val title: String,
    val name: String,
    val isManager: Boolean,
    val role: Role,
    val profileImageUrl: String?,
)