package com.teampatch.core.domain.usecase.question

import com.teampatch.core.domain.model.question.QuestionDetail
import com.teampatch.core.domain.repository.QuestionRepository
import javax.inject.Inject

class GetQuestionDetailUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {

    suspend operator fun invoke(questionId: String): QuestionDetail = questionRepository.getQuestionDetail(questionId)
}