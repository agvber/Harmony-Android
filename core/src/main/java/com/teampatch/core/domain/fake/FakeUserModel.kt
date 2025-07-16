package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.user.Role
import com.teampatch.core.domain.model.user.User

class FakeUserModel : FakeModel<List<User>>() {
    override fun build(): List<User> {
        return listOf(
            User(
                uid = "uid001",
                groupId = 1,
                name = "Alice Johnson",
                relation = "Mother",
                profileImageUrl = null,
                role = Role.VIP
            ),
            User(
                uid = "uid002",
                groupId = 2,
                name = "Bob Smith",
                relation = "Father",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid003",
                groupId = 3,
                name = "Charlie Brown",
                relation = "Older Brother",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid004",
                groupId = 4,
                name = "Dana White",
                relation = "Older Sister",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid005",
                groupId = 5,
                name = "Eve Black",
                relation = "Younger Sister",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid006",
                groupId = 6,
                name = "Frank Green",
                relation = "Younger Brother",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid007",
                groupId = 7,
                name = "Grace Lee",
                relation = "Aunt",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid008",
                groupId = 8,
                name = "Hank Miller",
                relation = "Uncle",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid009",
                groupId = 9,
                name = "Ivy Wilson",
                relation = "Grandmother",
                profileImageUrl = null,
                role = Role.MEMBER
            ),
            User(
                uid = "uid010",
                groupId = 10,
                name = "Jack King",
                relation = "Grandfather",
                profileImageUrl = null,
                role = Role.MEMBER
            )
        )
    }
}