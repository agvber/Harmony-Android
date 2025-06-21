package com.teampatch.harmony

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.teampatch.core.common.findActivity
import com.teampatch.feature.answer.addAnswerScreen
import com.teampatch.feature.answer.navigateToAnswerScreen
import com.teampatch.feature.daily.edit.navigateToDailyEditScreen
import com.teampatch.feature.daily.expand.addDailyExpandScreen
import com.teampatch.feature.family.info.addFamilyInfoScreen
import com.teampatch.feature.family.info.navigateToFamilyInfoScreen
import com.teampatch.feature.home.HomeRoute
import com.teampatch.feature.home.addHomeScreen
import com.teampatch.feature.home.navigateToHomeScreen
import com.teampatch.feature.login.LoginRoute
import com.teampatch.feature.login.addLoginScreen
import com.teampatch.feature.memory.chat.addMemoryChatScreen
import com.teampatch.feature.memory.chat.navigateToMemoryChatScreen
import com.teampatch.feature.memory.detail.addMemoryDetailScreen
import com.teampatch.feature.memory.detail.navigateToMemoryDetailScreen
import com.teampatch.feature.memory.registration.addMemoryRegistrationScreen
import com.teampatch.feature.memory.registration.navigateToMemoryRegistrationScreen
import com.teampatch.feature.memory.storage.addMemoryStorageScreen
import com.teampatch.feature.memory.storage.navigateToMemoryStorageScreen
import com.teampatch.feature.onboarding.admission.addOnboardingGroupAdmissionScreen
import com.teampatch.feature.onboarding.admission.navigateToOnboardingGroupAdmissionScreen
import com.teampatch.feature.onboarding.invitation.addOnboardingInputInvitationScreen
import com.teampatch.feature.onboarding.invitation.navigateToOnboardingInputInvitationScreen
import com.teampatch.feature.onboarding.management.OnboardingGroupManagementRoute
import com.teampatch.feature.onboarding.management.addOnboardingGroupManagementScreen
import com.teampatch.feature.onboarding.management.navigateToOnboardingGroupManagementScreen
import com.teampatch.feature.onboarding.permission.addOnboardingPermissionScreen
import com.teampatch.feature.onboarding.permission.navigateToOnboardingPermissionScreen
import com.teampatch.feature.onboarding.profile.addOnboardingProfileSettingsScreen
import com.teampatch.feature.onboarding.profile.navigateToOnboardingProfileSettingsScreen
import com.teampatch.feature.onboarding.relation.addOnboardingInputManagerInformationScreen
import com.teampatch.feature.onboarding.relation.navigateToOnboardingInputManagerInformationScreen
import com.teampatch.feature.onboarding.vip.addOnboardingInputVipInformationScreen
import com.teampatch.feature.onboarding.vip.navigateToOnboardingInputVipInformationScreen
import com.teampatch.feature.profile.edit.addProfileEditScreen
import com.teampatch.feature.profile.edit.navigateToProfileEditScreen
import com.teampatch.feature.question.addQuestionScreen
import com.teampatch.feature.question.detail.QuestionDetailParams
import com.teampatch.feature.question.detail.addQuestionDetailScreen
import com.teampatch.feature.question.detail.navigateToQuestionDetailScreen
import com.teampatch.feature.question.expand.addQuestionExpandScreen
import com.teampatch.feature.question.expand.navigateToQuestionExpandScreen
import com.teampatch.feature.settings.addSettingsScreen
import com.teampatch.feature.settings.navigateToSettingsScreen
import com.teampatch.harmony.model.MainUiState
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
fun MainNavHost(
    mainUiState: MainUiState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context: Context = LocalContext.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = getStartDestination(mainUiState)
    ) {
        addLoginScreen(
            onHomeScreenRequest = navController::navigateToHomeScreenWithBackStackClear,
            onPermissionNotificationRequest = navController::navigateToOnboardingPermissionScreen,
            onStartScreenRequest = navController::navigateToOnboardingGroupManagementScreen
        )

        addOnboardingPermissionScreen(
            onNextPageRequest = navController::navigateToOnboardingGroupManagementScreen
        )

        addOnboardingGroupManagementScreen(
            onBackRequest = navController::navigateUp,
            onGroupCreateRequest = navController::navigateToOnboardingInputVipInformationScreen,
            onGroupJoinRequest = navController::navigateToOnboardingInputInvitationScreen
        )

        addOnboardingInputVipInformationScreen(
            onBackRequest = navController::navigateUp,
            onNextPageRequest = { alias: String, name: String ->
                navController.navigateToOnboardingInputManagerInformationScreen()
            }
        )

        addOnboardingInputManagerInformationScreen(
            onBackRequest = navController::navigateUp,
            onNextPageRequest = { relation: String, name: String ->
                navController.navigateToOnboardingProfileSettingsScreen()
            }
        )

        addOnboardingProfileSettingsScreen(
            onBackRequest = navController::navigateUp,
            onNextPageRequest = { uri: Uri ->
                navController.navigateToOnboardingGroupAdmissionScreen()
            }
        )

        addOnboardingInputInvitationScreen(
            onBackRequest = navController::navigateUp,
            onNextPageRequest = navController::navigateToOnboardingInputManagerInformationScreen
        )

        addOnboardingGroupAdmissionScreen(
            onBackRequest = navController::navigateUp,
            onHomeRouteRequest = navController::navigateToHomeScreenWithBackStackClear
        )

        addHomeScreen(
            onUserPageRequest = navController::navigateToFamilyInfoScreen,
            onDailyRoutineClick = { },
            onDailyRoutineRegisterPageRequest = { },
            onMemoryCardClick = navController::navigateToMemoryRegistrationScreen
        )

        addMemoryStorageScreen(onDetailPageRequest = navController::navigateToMemoryDetailScreen)

        addMemoryRegistrationScreen(
            onDismissRequest = navController::navigateUp,
            onMemoryStorePageRequest = navController::navigateToMemoryStorageScreen
        )

        addMemoryDetailScreen(
            onBackRequest = navController::navigateUp,
            onDetailPageRequest = navController::navigateToMemoryChatScreen
        )

        addMemoryChatScreen(
            onCloseRequest = navController::navigateUp,
            onReplyChat = navController::navigateToMemoryRegistrationScreen,
        )

        addQuestionScreen(
            questionDetailPageRequest = navController::navigateToQuestionDetailScreen,
            answerPageRequest = navController::navigateToAnswerScreen,
            questionExpandPageRequest = navController::navigateToQuestionExpandScreen
        )

        addQuestionExpandScreen(
            onBackRequest = navController::navigateUp,
            questionDetailPageRequest = navController::navigateToQuestionDetailScreen
        )

        addQuestionDetailScreen(
            onBackRequest = navController::navigateUp,
            answerEditPageRequest = navController::navigateToAnswerScreen
        )

        addAnswerScreen(
            onBackRequest = navController::navigateUp,
            onCompleteRequest = { answer ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    key = QuestionDetailParams.ANSWER_UPDATE_DATA,
                    value = answer
                )
                navController.popBackStack()
            }
        )

        addSettingsScreen(
            onBackRequest = navController::navigateUp,
            onExitAppRequest = { context.findActivity()?.finishAffinity() },
            onPrivacyPolicyClick = { },
            onTosClick = { }
        )

        addFamilyInfoScreen(
            onBackRequest = navController::navigateUp,
            onSettingsClick = navController::navigateToSettingsScreen,
            onProfileEditClick = navController::navigateToProfileEditScreen
        )

        addProfileEditScreen(
            onCompleteRequest = navController::navigateUp
        )

        addDailyScreen(
            dailyExpandPageRequest = { navController.navigateToDailyScreen() }
        )

        addDailyExpandScreen(
            onBackRequest = navController::navigateUp,
            dailyEditPageRequest = { navController.navigateToDailyEditScreen() },
            onDeleteClick = {} // 임시
        )
    }
}

private fun NavHostController.navigateToHomeScreenWithBackStackClear() {
    navigateToHomeScreen(
        navOptions = navOptions {
            popUpTo(graph.id) { inclusive = true }
        }
    )
}

private fun getStartDestination(mainUiState: MainUiState): Any {
    return if (mainUiState.isFirstUser) {
        LoginRoute
    } else if (!mainUiState.isOnboardingComplete) {
        OnboardingGroupManagementRoute
    } else {
        HomeRoute
    }
}