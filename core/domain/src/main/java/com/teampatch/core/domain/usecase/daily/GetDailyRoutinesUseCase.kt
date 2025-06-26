package com.teampatch.core.domain.usecase.daily

import androidx.paging.PagingData
import com.teampatch.core.domain.model.Todo
import com.teampatch.core.domain.repository.TodoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDailyRoutinesUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {

    operator fun invoke(): Flow<PagingData<Todo>> = todoRepository.getAllTodos()
}