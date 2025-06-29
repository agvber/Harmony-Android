package com.teampatch.core.domain.usecase.todo

import com.teampatch.core.domain.model.todo.Todo
import com.teampatch.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    operator fun invoke(date: LocalDate): Flow<Todo> {
        return combine(
            todoRepository.getTodosByDate(date),
            todoRepository.getTodoProgress(date)
        ) { todos, progress ->
            Todo(
                todos = todos,
                date = date,
                totalItemCount = progress.totalCount,
                finishedItemCount = progress.finishedCount,
                progress = (progress.finishedCount.toFloat() / progress.totalCount)
                    .let { if (it.isNaN()) 1f else it }
            )
        }
    }
}