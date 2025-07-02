package com.teampatch.core.domain.model.question

data class QuestionComment(
    val commentId: String,
    val writerUid: String,
    val writerName: String,
    val content: String,
)