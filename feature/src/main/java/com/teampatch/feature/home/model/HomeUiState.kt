package com.teampatch.feature.home.model

import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.core.domain.model.user.Role
import java.time.LocalDate

internal data class HomeUiState(
    val role: Role = Role.MEMBER,
    val now: LocalDate = LocalDate.now(),
    val memoryCard: MemoryCard? = null
)