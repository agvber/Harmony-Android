package com.teampatch.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.harmony.core.database.dao.TodoDao
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

internal class TodoOfflineRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
) : TodoRepository {

    override fun getAllTodos(): Flow<PagingData<Todo>> = todoDao.getAllTodos().map { todos ->
        PagingData.from(todos).map {
            it.toDomain()
        }
    }

    override suspend fun toggleTodoStatus(id: String, isFinished: Boolean) {
        val todo = todoDao.getTodoById(id.toLong()).first()
            .copy(isFinished = isFinished)
        todoDao.updateTodo(todo.copy(isFinished = isFinished))
    }

    override suspend fun getDailyRoutineProgress(): Flow<Float> {
        return todoDao.getTodoCount().zip(todoDao.getFinishedCount()) { total, finished ->
            finished.toFloat() / total
        }
    }
}