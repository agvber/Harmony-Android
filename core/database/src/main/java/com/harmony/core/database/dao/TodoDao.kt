package com.harmony.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.harmony.core.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Long): Flow<TodoEntity>

    @Query("SELECT * FROM todo WHERE date(created_at) = date(:date)")
    fun getTodosByDate(date: String): Flow<List<TodoEntity>>

    @Query("SELECT COUNT(*) FROM todo WHERE is_finished = 1")
    fun getFinishedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM todo WHERE date(created_at) = date(:date) AND is_finished = 1")
    fun getFinishedCount(date: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM todo")
    fun getTodoCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM todo WHERE date(created_at) = date(:date)")
    fun getTodoCount(date: String): Flow<Int>

    @Insert
    suspend fun insertAll(vararg todos: TodoEntity)

    @Update(entity = TodoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(vararg todo: TodoEntity)

    @Query("DELETE FROM todo WHERE id = :id")
    fun deleteById(id: Long)

    @Delete
    fun delete(todo: TodoEntity)
}