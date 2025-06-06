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
import com.teampatch.feature.home.addHomeScreen
import com.teampatch.feature.home.navigateToHomeScreen
import com.teampatch.feature.memorycard.registration.addMemoryCardRegistrationScreen
import com.teampatch.feature.memorycard.registration.navigateToMemoryCardRegistrationScreen
import com.teampatch.feature.memorystorage.addMemoryStorageScreen
import com.teampatch.feature.onboarding.admission.addOnboardingGroupAdmissionScreen
import com.teampatch.feature.onboarding.admission.navigateToOnboardingGroupAdmissionScreen
import com.teampatch.feature.onboarding.invitation.addOnboardingInputInvitationScreen
import com.teampatch.feature.onboarding.invitation.navigateToOnboardingInputInvitationScreen
import com.teampatch.feature.login.LoginRoute
import com.teampatch.feature.login.addLoginScreen
import com.teampatch.feature.onboarding.management.OnboardingGroupManagementRoute
import com.teampatch.feature.onboarding.management.addOnboardingGroupManagementScreen
import com.teampatch.feature.onboarding.management.navigateToOnboardingGroupManagementScreen
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
import com.teampatch.memorystorage.feature.detail.addMemoryStorageDetailConversationScreen
import com.teampatch.memorystorage.feature.detail.addMemoryStorageDetailScreen
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
        startDestination = OnboardingGroupManagementRoute
    ) {
        addLoginScreen(
            onHomeScreenRequest = navController::navigateToHomeScreenWithBackStackClear,
            onPermissionNotificationRequest = navController::navigateToOnboardingPermissionScreen,
            onStartScreenRequest = navController::navigateToOnboardingGroupManagementScreen
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
            onNextPageRequest = navController::navigateToOnboardingGroupAdmissionScreen
        )

        addOnboardingGroupAdmissionScreen(
            onBackRequest = navController::navigateUp,
            onHomeRouteRequest = navController::navigateToHomeScreenWithBackStackClear
        )

        addHomeScreen(
            onUserPageRequest = navController::navigateToFamilyInfoScreen,
            onDailyRoutineClick = { },
            onDailyRoutineRegisterPageRequest = { },
            onMemoryCardClick = navController::navigateToMemoryCardRegistrationScreen
        )

        addMemoryStorageScreen(
            onDetailPageRequest = {}
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

        addMemoryCardRegistrationScreen(
            onDismissRequest = navController::navigateUp,
            onMemoryStorePageRequest = { } // TODO: 메모리 저장소 페이지 가기
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

        addMemoryStorageDetailScreen(
            onBackRequest = navController::navigateUp,
            onRestartConversation = { navController.navigateToMemoryCardRegistrationScreen("memoryCardId") }
        )

        addMemoryStorageDetailConversationScreen(
            onDismiss = navController::navigateUp,
            onRestartConversation = { navController.navigateToMemoryCardRegistrationScreen("memoryCardId") }
        )
    }
}

private fun NavHostController.navigateToHomeScreenWithBackStackClear() {
    navigateToHomeScreen(
        navOptions = navOptions {
            popUpTo(LoginRoute) {
                inclusive = true
            }
            launchSingleTop = true
        }
    )
}