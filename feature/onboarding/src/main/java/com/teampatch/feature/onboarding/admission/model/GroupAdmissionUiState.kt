package com.teampatch.feature.onboarding.admission.model

import android.net.Uri

data class GroupAdmissionUiState(
    val name: String,
    val vipRelation: String,
    val members: List<Member>
) {

    data class Member(
        val profileImageUri: Uri?
    )

    companion object {
        fun init(): GroupAdmissionUiState =
            GroupAdmissionUiState(name = "", vipRelation = "", members = emptyList())
    }
}