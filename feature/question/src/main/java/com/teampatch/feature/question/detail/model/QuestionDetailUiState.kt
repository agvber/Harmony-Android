package com.teampatch.feature.question.detail.model

import com.teampatch.core.domain.model.user.Role
import java.time.LocalDateTime

internal data class QuestionDetailUiState(
    val uid: String = "",
    val role: Role = Role.VIP,
    val post: Post = Post(
        id = "",
        number = 0,
        title = "",
        content = "",
        dateTime = LocalDateTime.MIN,
        hasWritePermission = false
    ),
    val isLoading: Boolean = true,
) {
    val postWritable: Boolean = role == Role.VIP
}