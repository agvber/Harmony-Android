package com.teampatch.core.domain.model

data class AdmissionGroupInformation(
    val groupId: String,
    val manager: Manager,
    val users: List<User>
) {

    data class Manager(
        val uid: Long,
        val vipRelation: String,
        val name: String,
        val profileImageUrl: String?
    )
}