package com.teampatch.feature.question.expand

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object QuestionExpandRoute

fun NavController.navigateToQuestionExpandScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(QuestionExpandRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addQuestionExpandScreen(
    onBackRequest: () -> Unit,
    questionDetailPageRequest: (String) -> Unit,
) {
    composable<QuestionExpandRoute> {
        QuestionExpandRoute(
            onBackRequest = onBackRequest,
            questionDetailPageRequest = questionDetailPageRequest
        )
    }
}