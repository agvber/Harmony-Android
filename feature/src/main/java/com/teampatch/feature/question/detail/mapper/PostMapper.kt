package com.teampatch.feature.question.detail.mapper

import com.teampatch.core.domain.model.question.QuestionDetail
import com.teampatch.feature.question.detail.model.Post

internal fun QuestionDetail.toPresentationModel(hasWritePermission: Boolean): Post = Post(
    id = id,
    number = number,
    title = title,
    content = content,
    dateTime = dateTime,
    hasWritePermission = hasWritePermission
)