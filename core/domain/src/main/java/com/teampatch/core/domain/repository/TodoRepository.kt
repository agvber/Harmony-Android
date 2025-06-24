package com.teampatch.core.domain.repository

import androidx.paging.PagingData
import com.teampatch.core.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getAllTodos(): Flow<PagingData<Todo>>

    suspend fun toggleTodoStatus(id: String, isFinished: Boolean)

    suspend fun getDailyRoutineProgress(): Flow<Float>
}