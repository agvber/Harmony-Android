package com.teampatch.feature.daily.main.model

import com.teampatch.core.domain.model.Role
import java.time.LocalDateTime

internal data class DailyMainUiState(
    val now: LocalDateTime = LocalDateTime.now(),
    val progress: Float = 0f, // 0.0 - 1.0
    val role: Role = Role.VIP,
    val isLoading: Boolean = true,
)