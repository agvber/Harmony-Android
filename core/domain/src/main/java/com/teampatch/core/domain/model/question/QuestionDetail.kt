package com.teampatch.core.domain.model.question

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

data class QuestionDetail(
    val id: String,
    val number: Int,
    val title: String,
    val content: String,
    val dateTime: LocalDateTime,
    val commentCount: Int,
    val comment: Flow<PagingData<QuestionComment>> = flowOf(PagingData.Companion.empty()),
)