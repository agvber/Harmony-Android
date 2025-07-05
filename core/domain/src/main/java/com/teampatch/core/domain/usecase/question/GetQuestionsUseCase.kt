package com.teampatch.core.domain.usecase.question

import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {

    operator fun invoke(limit: Int = -1): Flow<List<Question>> =
        questionRepository.getQuestions(limit)
}