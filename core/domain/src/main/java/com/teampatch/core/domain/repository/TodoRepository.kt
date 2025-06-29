package com.teampatch.core.domain.repository

import androidx.paging.PagingData
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.model.todo.TodoProgress
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TodoRepository {

    fun getAllTodos(): Flow<PagingData<Todo>>

    fun getTodosByDate(date: LocalDate): Flow<List<Todo>>

    suspend fun toggleTodoStatus(id: String, isFinished: Boolean)

    suspend fun getDailyRoutineProgress(): Flow<Float>

    fun getTodoProgress(date: LocalDate): Flow<TodoProgress>
}