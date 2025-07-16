package com.teampatch.core.domain.usecase.answer

import com.teampatch.core.domain.repository.AnswerRepository
import javax.inject.Inject

class AddAnswerUseCase @Inject constructor(
    private val answerRepository: AnswerRepository,
) {

    suspend operator fun invoke(questionId: String, answer: String) {
        answerRepository.addAnswer(questionId, answer)
    }
}