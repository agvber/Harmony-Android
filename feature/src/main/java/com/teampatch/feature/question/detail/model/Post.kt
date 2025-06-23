package com.teampatch.feature.question.detail.model

import java.time.LocalDateTime

data class Post(
    val id: String,
    val number: Int,
    val title: String,
    val content: String,
    val dateTime: LocalDateTime,
    val hasWritePermission: Boolean,
)