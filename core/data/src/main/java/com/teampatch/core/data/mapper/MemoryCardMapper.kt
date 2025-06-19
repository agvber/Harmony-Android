package com.teampatch.core.data.mapper

import com.harmony.core.database.LOCAL_DB_DATE_TIME_FORMATTER
import com.harmony.core.database.model.MemoryCardEntity
import com.teampatch.core.domain.model.MemoryCard
import java.time.LocalDateTime

fun MemoryCardEntity.toDomain(
    writerName: String,
): MemoryCard = MemoryCard(
    id = id.toString(),
    writerTitle = title,
    writerName = writerName,
    text = content,
    imageUrl = imageUrl,
    dateTime = LocalDateTime.parse(
        /* text = */
        modifiedAt,
        /* formatter = */
        LOCAL_DB_DATE_TIME_FORMATTER
    ),
    tags = tags
)