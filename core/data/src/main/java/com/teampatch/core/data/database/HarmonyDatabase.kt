package com.teampatch.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teampatch.core.data.database.converters.SetStringTypeTypeConverter
import com.teampatch.core.data.database.dao.GroupDao
import com.teampatch.core.data.database.dao.MemoryCardDao
import com.teampatch.core.data.database.dao.QuestionDao
import com.teampatch.core.data.database.dao.TodoDao
import com.teampatch.core.data.database.dao.UserDao
import com.teampatch.core.data.database.model.GroupEntity
import com.teampatch.core.data.database.model.MemoryCardEntity
import com.teampatch.core.data.database.model.QuestionCommentEntity
import com.teampatch.core.data.database.model.QuestionEntity
import com.teampatch.core.data.database.model.TodoEntity
import com.teampatch.core.data.database.model.UserEntity

@Database(
    entities = [
        TodoEntity::class,
        UserEntity::class,
        GroupEntity::class,
        QuestionEntity::class,
        QuestionCommentEntity::class,
        MemoryCardEntity::class
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

    companion object {
        internal const val DB_NAME = "harmony.db"
    }
}