package com.teampatch.core.data.database.model.preload

import android.content.ContentValues

internal class UserPreloadData : DataPreloadHelper() {

    companion object {
        private const val TABLE_NAME = "user"
    }

    override val tableName: String = TABLE_NAME

    override val preloadData: List<ContentValues> = listOf(
        ContentValues().apply {
            put("uid", 9999)
            put("name", "operator")
            put("relation", "operator")
            put("role", "v")
            put("sns_id", "9999999999")
        }
    )
}