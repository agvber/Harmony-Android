package com.teampatch.core.domain.model

import java.time.LocalDateTime

data class MemoryCard(
    val id: String,
    val writerTitle: String,
    val writerName: String,
    val text: String,
    val imageUrl: String?,
    val imageUri: String?,
    val dateTime: LocalDateTime,
    val tags: Set<String>,
)