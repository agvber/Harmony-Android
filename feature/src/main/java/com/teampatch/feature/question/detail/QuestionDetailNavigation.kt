package com.teampatch.feature.question.detail

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.teampatch.feature.question.detail.QuestionDetailParams.ANSWER_UPDATE_DATA
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDetailRoute(val questionId: String)

object QuestionDetailParams {
    const val ANSWER_UPDATE_DATA = "answer_update_data"
}

fun NavController.navigateToQuestionDetailScreen(
    questionId: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = QuestionDetailRoute(questionId),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addQuestionDetailScreen(
    onBackRequest: () -> Unit,
    answerEditPageRequest: (questionId: String) -> Unit,
) {
    composable<QuestionDetailRoute> { navBackStackEntry ->
        val savedStateHandle: SavedStateHandle = navBackStackEntry.savedStateHandle
        val viewModel: QuestionDetailViewModel = hiltViewModel(navBackStackEntry)

        QuestionDetailRoute(
            onBackRequest = onBackRequest,
            answerEditPageRequest = answerEditPageRequest,
            viewModel = viewModel
        )

        LaunchedEffect(Unit) {
            savedStateHandle
                .getStateFlow(ANSWER_UPDATE_DATA, "")
                .collectLatest { answer: String ->
                    if (answer.isNotBlank()) {
                        viewModel.updateQuestionAnswer(answer)
                    }
                }
        }
    }
}