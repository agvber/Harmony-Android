package com.harmony.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.harmony.core.database.model.QuestionCommentEntity
import com.harmony.core.database.model.QuestionEntity
import com.harmony.core.database.model.multimap.QuestionCommentWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question limit :limit")
    fun getQuestions(limit: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM question WHERE id = :questionId")
    fun getQuestionById(questionId: Long): Flow<QuestionEntity>

    @Query(
        """
        SELECT QC.*, user.*
        FROM question_comment QC
        INNER JOIN user ON QC.written_uid = user.uid
        WHERE QC.question_id = :questionId
        ORDER BY QC.modified_at DESC
    """
    )
    fun getQuestionComments(questionId: Long): Flow<List<QuestionCommentWithUser>>

    @Query("SELECT * FROM question_comment WHERE id = :questionCommentId")
    fun getQuestionCommentById(questionCommentId: Long): Flow<QuestionCommentEntity>

    @Insert(entity = QuestionEntity::class)
    suspend fun insertQuestion(vararg questionEntity: QuestionEntity): List<Long>

    @Insert(entity = QuestionCommentEntity::class)
    suspend fun insertQuestionComment(vararg questionCommentEntity: QuestionCommentEntity): List<Long>

    @Update(entity = QuestionCommentEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestionComment(questionCommentEntity: QuestionCommentEntity)

    @Query("DELETE FROM question_comment WHERE id = :questionCommentId")
    suspend fun deleteQuestionComment(questionCommentId: Long)
}