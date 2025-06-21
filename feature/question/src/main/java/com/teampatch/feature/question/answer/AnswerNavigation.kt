package com.teampatch.feature.question.answer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class AnswerRoute(val questionId: String)

fun NavController.navigateToAnswerScreen(
    questionId: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = AnswerRoute(questionId),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addAnswerScreen(
    onBackRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
) {
    composable<AnswerRoute> {
        AnswerRoute(onBackRequest = onBackRequest, onCompleteRequest = onCompleteRequest)
    }
}