package com.teampatch.feature.memory.chat.model

import com.teampatch.core.domain.model.Role
import java.time.LocalDate

internal data class MemoryChatUiState(
    val id: String = "",
    val title: String = "",
    val question: String = "",
    val answer: String? = "",
    val imageUrl: String? = null,
    val date: LocalDate = LocalDate.MIN,
    val role: Role = Role.MEMBER,
)