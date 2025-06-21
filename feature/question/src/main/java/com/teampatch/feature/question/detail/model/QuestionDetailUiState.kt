package com.teampatch.feature.question.detail.model

import androidx.paging.PagingData
import com.teampatch.core.common.PagingDataHelper
import java.time.LocalDateTime
import kotlinx.coroutines.flow.flowOf

internal data class QuestionDetailUiState(
    val post: Post = Post("", 0, "", "", LocalDateTime.now(), false),
    val comments: PagingDataHelper<Comment> = PagingDataHelper(flowOf(PagingData.empty())),
    val isLoading: Boolean = true,
)