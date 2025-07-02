package com.teampatch.feature.question.main.model

import androidx.paging.PagingData
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class QuestionUiState(
    val user: User = User.createEmptyUser(),
    val question: Flow<PagingData<Question>> = emptyFlow(),
    val isLoading: Boolean = true,
)