package com.teampatch.core.domain.usecase.question

import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.repository.QuestionRepository
import javax.inject.Inject

class AddQuestionCommentUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {

    suspend operator fun invoke(questionId: String, comment: String): QuestionComment = questionRepository.addComment(questionId, comment)
}