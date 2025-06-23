package com.teampatch.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "memory_card",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"]
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["written_uid"]
        )
    ]
)
data class MemoryCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo("question_id") val questionId: Long,
    @ColumnInfo("written_uid") val writtenUid: Long,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("modified_at") val modifiedAt: String,
    @ColumnInfo("image_uri") val imageUri: String?,
    @ColumnInfo("image_url") val imageUrl: String?,
    @ColumnInfo("tags") val tags: Set<String>,
)