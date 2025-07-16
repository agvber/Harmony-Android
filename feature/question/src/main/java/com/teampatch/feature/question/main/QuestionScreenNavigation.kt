package com.teampatch.feature.question.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object QuestionMainRoute

fun NavController.navigateToQuestionMainScreen(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
        restoreState = true

        popUpTo(QuestionMainRoute) {
            inclusive = true
        }
    },
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(route = QuestionMainRoute, navOptions = navOptions, navigatorExtras = navigatorExtras)
}

fun NavGraphBuilder.addQuestionMainScreen(
    questionDetailPageRequest: (String) -> Unit,
    answerPageRequest: (String) -> Unit,
    questionExpandPageRequest: () -> Unit,
) {
    composable<QuestionMainRoute> {
        QuestionMainScreenWithViewModel(
            questionDetailPageRequest = questionDetailPageRequest,
            answerPageRequest = answerPageRequest,
            questionExpandPageRequest = questionExpandPageRequest
        )
    }
}