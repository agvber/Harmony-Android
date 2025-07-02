package com.teampatch.core.domain.usecase.question

import androidx.paging.PagingData
import com.teampatch.core.domain.fake.FakeQuestionComments
import com.teampatch.core.domain.model.question.QuestionComment
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetQuestionCommentsUseCase @Inject constructor() {

    operator fun invoke(questionId: String): Flow<PagingData<QuestionComment>> = flowOf(PagingData.from(FakeQuestionComments().get()))
}