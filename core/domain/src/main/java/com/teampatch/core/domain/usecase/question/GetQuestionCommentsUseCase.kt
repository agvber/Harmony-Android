package com.teampatch.core.domain.usecase.question

import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionCommentsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {

    operator fun invoke(questionId: String): Flow<List<QuestionComment>> =
        questionRepository.getQuestionComments(questionId)
}