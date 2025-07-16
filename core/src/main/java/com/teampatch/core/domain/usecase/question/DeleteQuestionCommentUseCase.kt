package com.teampatch.core.domain.usecase.question

import com.teampatch.core.domain.repository.QuestionRepository
import javax.inject.Inject

class DeleteQuestionCommentUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {

    suspend operator fun invoke(commentId: String) {
        questionRepository.deleteComment(commentId)
    }
}