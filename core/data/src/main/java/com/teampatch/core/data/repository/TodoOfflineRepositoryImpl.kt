package com.teampatch.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.harmony.core.database.LOCAL_DB_DATE_FORMATTER
import com.harmony.core.database.dao.TodoDao
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.model.todo.TodoProgress
import com.teampatch.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

internal class TodoOfflineRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
) : TodoRepository {

    override fun getAllTodos(): Flow<PagingData<Todo>> = todoDao.getAllTodos().map { todos ->
        PagingData.from(todos).map {
            it.toDomain()
        }
    }

    override fun getTodosByDate(date: LocalDate): Flow<List<Todo>> {
        val dateStringFormat = date.format(LOCAL_DB_DATE_FORMATTER)
        return todoDao.getTodosByDate(dateStringFormat)
            .map { it.map { todo -> todo.toDomain() } }
    }

    override suspend fun toggleTodoStatus(id: String, isFinished: Boolean) {
        val todo = todoDao.getTodoById(id.toLong()).first()
            .copy(isFinished = isFinished)
        todoDao.updateTodo(todo.copy(isFinished = isFinished))
    }

    override fun getTodoProgress(date: LocalDate): Flow<TodoProgress> {
        val dateStringFormat: String = date.format(LOCAL_DB_DATE_FORMATTER)
        return todoDao.getTodoCount(dateStringFormat)
            .combine(todoDao.getFinishedCount(dateStringFormat)) { total, finished ->
                TodoProgress(total, finished)
            }
    }
}