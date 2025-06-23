package com.teampatch.core.data.database.model.multimap

import androidx.room.Embedded
import com.teampatch.core.data.database.model.QuestionCommentEntity
import com.teampatch.core.data.database.model.UserEntity

data class QuestionCommentWithUser(
    @Embedded val comment: QuestionCommentEntity,
    @Embedded val user: UserEntity,
)