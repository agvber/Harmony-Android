package com.teampatch.core.domain.entities

import javax.inject.Inject

class Group @Inject constructor() {

    fun checkInviteCode(inviteCode: Int) {
        require(inviteCode.toString().length == INVITE_CODE_LENGTH)
    }

    fun checkExistGroup(groupId: Int): Boolean {
        return groupId != IS_NOT_GROUP_CODE
    }

    companion object {
        private const val INVITE_CODE_LENGTH: Int = 5
        private const val IS_NOT_GROUP_CODE: Int = -1
    }
}