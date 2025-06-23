package com.teampatch.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.teampatch.core.data.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Long): Flow<TodoEntity>

    @Insert
    suspend fun insertAll(vararg todos: TodoEntity)

    @Update(entity = TodoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(vararg todo: TodoEntity)

    @Query("DELETE FROM todo WHERE id = :id")
    fun deleteById(id: Long)

    @Delete
    fun delete(todo: TodoEntity)
}