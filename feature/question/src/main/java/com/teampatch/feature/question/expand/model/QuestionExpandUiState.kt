package com.teampatch.feature.question.expand.model

import androidx.paging.PagingData
import com.teampatch.core.domain.model.question.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class QuestionExpandUiState(
    val question: Flow<PagingData<Question>> = emptyFlow(),
    val isLoading: Boolean = true,
)