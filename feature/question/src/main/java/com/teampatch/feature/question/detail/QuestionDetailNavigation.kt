package com.teampatch.feature.question.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDetailRoute(val questionId: String)

private const val ANSWER_UPDATE_DATA = "answer_update_data"

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

fun NavController.popBackStackToQuestionDetailRouteResult(answer: String) {
    previousBackStackEntry?.savedStateHandle?.set(
        key = ANSWER_UPDATE_DATA,
        value = answer
    )
    popBackStack()
}

fun NavGraphBuilder.addQuestionDetailScreen(
    onBackRequest: () -> Unit,
    answerEditPageRequest: (questionId: String) -> Unit,
) {
    composable<QuestionDetailRoute> { navBackStackEntry ->
        QuestionDetailRoute(
            onBackRequest = onBackRequest,
            answerEditPageRequest = answerEditPageRequest,
            answer = navBackStackEntry.savedStateHandle[ANSWER_UPDATE_DATA] ?: ""
        )
    }
}