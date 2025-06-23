package com.teampatch.feature.memory.detail.model

import com.teampatch.core.domain.model.Role
import java.time.LocalDate

data class MemoryDetailUiState(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val writtenDateTime: LocalDate = LocalDate.MIN,
    val tags: Set<String> = emptySet(),
    val imageUrl: String? = null,
    val role: Role = Role.VIP
)