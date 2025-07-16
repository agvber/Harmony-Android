package com.teampatch.core.domain.usecase.answer

import com.teampatch.core.domain.repository.AnswerRepository
import javax.inject.Inject

class EditAnswerUseCase @Inject constructor(
    private val answerRepository: AnswerRepository,
) {

    suspend operator fun invoke(
        questionId: String,
        answer: String,
    ) {
        answerRepository.editAnswer(questionId, answer)
    }
}