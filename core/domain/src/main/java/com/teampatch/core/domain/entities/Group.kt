package com.teampatch.core.domain.entities

import javax.inject.Inject

class Group @Inject constructor() {

    fun checkInviteCode(inviteCode: Int) {
        require(inviteCode.toString().length == INVITE_CODE_LENGTH)
    }

    companion object {
        private const val INVITE_CODE_LENGTH: Int = 5
    }
}