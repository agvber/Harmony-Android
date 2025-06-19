package com.teampatch.feature.question

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object QuestionRoute

fun NavController.navigateToQuestionScreen(
    navOptions: NavOptions? = navOptions { launchSingleTop = true },
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(route = QuestionRoute, navOptions = navOptions, navigatorExtras = navigatorExtras)
}

fun NavGraphBuilder.addQuestionScreen(
    questionDetailPageRequest: (String) -> Unit,
    answerPageRequest: (String) -> Unit,
    questionExpandPageRequest: () -> Unit,
) {
    composable<QuestionRoute> {
        QuestionRoute(
            questionDetailPageRequest = questionDetailPageRequest,
            answerPageRequest = answerPageRequest,
            questionExpandPageRequest = questionExpandPageRequest
        )
    }
}