package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.group.FamilyInfo
import com.teampatch.core.domain.model.user.Role

class FakeFamilyInfo : FakeModel<List<FamilyInfo>>() {
    override fun build(): List<FamilyInfo> = listOf(
        FamilyInfo(
            title = "Mr.",
            name = "John Doe",
            isManager = true,
            role = Role.VIP,
            profileImageUrl = FAKE_IMAGE_URL
        ),
        FamilyInfo(
            title = "Ms.",
            name = "Jane Doe",
            isManager = false,
            role = Role.MEMBER,
            profileImageUrl = FAKE_IMAGE_URL
        ),
        FamilyInfo(
            title = "Dr.",
            name = "Sam Smith",
            isManager = false,
            role = Role.MEMBER,
            profileImageUrl = FAKE_IMAGE_URL
        ),
        FamilyInfo(
            title = "Mrs.",
            name = "Anna Brown",
            isManager = true,
            role = Role.VIP,
            profileImageUrl = FAKE_IMAGE_URL
        )
    )
}