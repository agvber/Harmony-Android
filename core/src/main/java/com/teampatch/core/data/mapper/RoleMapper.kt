package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.user.Role

internal const val MEMBER = "m"
internal const val VIP = "v"

internal fun roleStringMapper(role: String): Role = when (role) {
    VIP -> Role.VIP
    MEMBER -> Role.MEMBER
    else -> throw IllegalArgumentException("Invalid role: $role")
}