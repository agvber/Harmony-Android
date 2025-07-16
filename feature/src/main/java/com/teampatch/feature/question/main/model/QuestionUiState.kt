package com.teampatch.feature.question.main.model

import com.teampatch.core.domain.model.user.Role

internal data class QuestionUiState(
    val role: Role = Role.MEMBER,
    val isLoading: Boolean = true,
)