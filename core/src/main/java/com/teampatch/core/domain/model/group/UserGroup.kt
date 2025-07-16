package com.teampatch.core.domain.model.group

data class UserGroup(
    val groupId: String,
    val name: String,
    val members: List<FamilyInfo>,
)