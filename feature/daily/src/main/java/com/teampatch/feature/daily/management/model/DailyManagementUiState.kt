package com.teampatch.feature.daily.management.model

import com.teampatch.core.domain.model.DailyManage

internal data class DailyManagementUiState(
    val dailyManage: DailyManage? = null,
    val isLoading: Boolean = true,
)