package com.teampatch.core.domain.usecase.question

import com.teampatch.core.domain.repository.QuestionRepository
import javax.inject.Inject

class EditQuestionCommentUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {

    suspend operator fun invoke(
        commentId: String,
        comment: String,
    ) {
        questionRepository.editComment(commentId, comment)
    }
}