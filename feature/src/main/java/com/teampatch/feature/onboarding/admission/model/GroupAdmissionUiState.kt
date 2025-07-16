package com.teampatch.feature.onboarding.admission.model

import android.net.Uri

data class GroupAdmissionUiState(
    val manager: Manager,
    val members: List<Member>,
    val memberSize: Int
) {

    data class Manager(
        val name: String,
        val vipRelation: String,
        val profileImageUri: Uri?
    )

    data class Member(
        val profileImageUri: Uri?
    )

    companion object {
        fun init(): GroupAdmissionUiState =
            GroupAdmissionUiState(
                manager = Manager(name = "", vipRelation = "", profileImageUri = null),
                members = emptyList(),
                memberSize = 0
            )
    }
}