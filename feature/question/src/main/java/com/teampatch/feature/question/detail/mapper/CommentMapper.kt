package com.teampatch.feature.question.detail.mapper

import com.teampatch.core.domain.model.QuestionComment
import com.teampatch.feature.question.detail.model.Comment

internal fun QuestionComment.toPresentationModel(currentUserName: String): Comment = Comment(
    id = commentId,
    content = content,
    writer = Comment.Writer(
        uid = writerUid,
        name = writerName
    ),
    hasWritePermission = currentUserName == writerName
)