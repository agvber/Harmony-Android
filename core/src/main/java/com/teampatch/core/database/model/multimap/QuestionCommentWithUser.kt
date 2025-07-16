package com.teampatch.core.database.model.multimap

import androidx.room.Embedded
import com.teampatch.core.database.model.QuestionCommentEntity
import com.teampatch.core.database.model.UserEntity

data class QuestionCommentWithUser(
    @Embedded val comment: QuestionCommentEntity,
    @Embedded val user: UserEntity,
)