package com.teampatch.core.data.mapper

import com.teampatch.core.data.database.LOCAL_DB_DATE_TIME_FORMATTER
import com.teampatch.core.data.database.model.TodoEntity
import com.teampatch.core.domain.model.Todo
import java.time.LocalDateTime

fun TodoEntity.toDomain() = Todo(
    id = id.toString(),
    dateTime = LocalDateTime.parse(createdAt, LOCAL_DB_DATE_TIME_FORMATTER),
    title = title,
    isFinished = isFinished
)