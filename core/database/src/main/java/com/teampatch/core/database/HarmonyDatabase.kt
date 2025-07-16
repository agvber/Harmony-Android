package com.teampatch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teampatch.core.database.converters.SetStringTypeTypeConverter
import com.teampatch.core.database.dao.GroupDao
import com.teampatch.core.database.dao.MemoryCardDao
import com.teampatch.core.database.dao.QuestionDao
import com.teampatch.core.database.dao.RoutineDao
import com.teampatch.core.database.dao.TodoDao
import com.teampatch.core.database.dao.UserDao
import com.teampatch.core.database.model.GroupEntity
import com.teampatch.core.database.model.MemoryCardEntity
import com.teampatch.core.database.model.QuestionCommentEntity
import com.teampatch.core.database.model.QuestionEntity
import com.teampatch.core.database.model.RoutineEntity
import com.teampatch.core.database.model.RoutineLogEntity
import com.teampatch.core.database.model.TodoEntity
import com.teampatch.core.database.model.UserEntity

@Database(
    entities = [
        TodoEntity::class,
        UserEntity::class,
        GroupEntity::class,
        QuestionEntity::class,
        QuestionCommentEntity::class,
        MemoryCardEntity::class,
        RoutineEntity::class,
        RoutineLogEntity::class,
    ],
    version = 1
)
@TypeConverters(
    value = [
        SetStringTypeTypeConverter::class
    ]
)
internal abstract class HarmonyDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun questionDao(): QuestionDao
    abstract fun memoryCardDao(): MemoryCardDao
    abstract fun routineDao(): RoutineDao

    companion object {
        internal const val DB_NAME = "harmony.db"
    }
}