package com.harmony.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.harmony.core.database.HarmonyDatabase
import com.harmony.core.database.dao.GroupDao
import com.harmony.core.database.dao.MemoryCardDao
import com.harmony.core.database.dao.QuestionDao
import com.harmony.core.database.dao.RoutineDao
import com.harmony.core.database.dao.TodoDao
import com.harmony.core.database.dao.UserDao
import com.harmony.core.database.model.preload.MemoryCardPreloadData
import com.harmony.core.database.model.preload.QuestionPreloadData
import com.harmony.core.database.model.preload.RoutinePreloadData
import com.harmony.core.database.model.preload.TodoPreloadData
import com.harmony.core.database.model.preload.UserPreloadData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Singleton
    @Provides
    fun providesRoomInstance(
        @ApplicationContext appContext: Context,
    ): HarmonyDatabase {
        val roomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                listOf(
                    QuestionPreloadData(),
                    TodoPreloadData(),
                    UserPreloadData(),
                    MemoryCardPreloadData(),
                    RoutinePreloadData(),
                )
                    .forEach { it.insertPreloadData(db) }
            }
        }

        return Room.databaseBuilder(
            appContext,
            HarmonyDatabase::class.java,
            HarmonyDatabase.DB_NAME
        )
            .addCallback(roomDatabaseCallback)
            .build()
    }

    @Provides
    fun providesTodoDao(
        harmonyDatabase: HarmonyDatabase,
    ): TodoDao = harmonyDatabase.todoDao()

    @Provides
    fun providesUserDao(
        harmonyDatabase: HarmonyDatabase,
    ): UserDao = harmonyDatabase.userDao()

    @Provides
    fun providesGroupDao(
        harmonyDatabase: HarmonyDatabase,
    ): GroupDao = harmonyDatabase.groupDao()

    @Provides
    fun providesQuestionDao(
        harmonyDatabase: HarmonyDatabase,
    ): QuestionDao = harmonyDatabase.questionDao()

    @Provides
    fun providesMemoryCardDao(
        harmonyDatabase: HarmonyDatabase,
    ): MemoryCardDao = harmonyDatabase.memoryCardDao()

    @Provides
    fun providesRoutineDao(
        harmonyDatabase: HarmonyDatabase,
    ): RoutineDao = harmonyDatabase.routineDao()
}