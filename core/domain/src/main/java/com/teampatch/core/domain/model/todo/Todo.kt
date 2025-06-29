package com.teampatch.core.domain.model.todo

import com.teampatch.core.domain.model.Todo
import java.time.LocalDate

data class Todo(
    val todos: List<Todo>,
    val date: LocalDate,
    val totalItemCount: Int,
    val finishedItemCount: Int,
    val progress: Float // 0.1 - 1.0
)