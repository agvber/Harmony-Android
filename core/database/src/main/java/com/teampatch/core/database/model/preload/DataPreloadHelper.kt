package com.teampatch.core.database.model.preload

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase

internal abstract class DataPreloadHelper {

    protected abstract val tableName: String

    protected abstract val preloadData: List<ContentValues>

    fun insertPreloadData(db: SupportSQLiteDatabase) {
        preloadData.forEach { values ->
            db.insert(tableName, OnConflictStrategy.IGNORE, values)
        }
    }
}