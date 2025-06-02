package com.teampatch.harmony

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
import com.teampatch.feature.memorycard.registration.addMemoryCardRegistrationScreen
import com.teampatch.feature.memorycard.registration.navigateToMemoryCardRegistrationScreen
import com.teampatch.feature.memorystorage.addMemoryStorageScreen
import com.teampatch.feature.onboarding.enter.addOnboardingEnterInvitationCodeScreen
import com.teampatch.feature.onboarding.enter.addOnboardingEnterProfileSettingsScreen
import com.teampatch.feature.onboarding.enter.addOnboardingEnterRelationScreen
import com.teampatch.feature.onboarding.enter.addOnboardingEnterSpaceScreen
import com.teampatch.feature.onboarding.enter.navigateToEnterInvitationCodeScreen
import com.teampatch.feature.onboarding.enter.navigateToEnterProfileSettingsScreen
import com.teampatch.feature.onboarding.enter.navigateToEnterRelationScreen
import com.teampatch.feature.onboarding.enter.navigateToEnterSpaceScreen
import com.teampatch.feature.onboarding.login.OnboardingRoute
import com.teampatch.feature.onboarding.login.addOnboardingScreen
import com.teampatch.feature.onboarding.make.addOnboardingMakeInviteGrandParentsScreen
import com.teampatch.feature.onboarding.make.addOnboardingMakeParentsNameScreen
import com.teampatch.feature.onboarding.make.addOnboardingMakeProfileSettingsScreen
import com.teampatch.feature.onboarding.make.addOnboardingMakeRelationScreen
import com.teampatch.feature.onboarding.make.navigateToMakeGroupScreen
import com.teampatch.feature.onboarding.make.navigateToMakeProfileSettingsScreen
import com.teampatch.feature.onboarding.make.navigateToMakeRelationScreen
import com.teampatch.feature.onboarding.make.navigateToShareInvitationScreen
import com.teampatch.feature.onboarding.management.OnboardingStartRoute
import com.teampatch.feature.onboarding.permission.addOnboardingPermissionScreen
import com.teampatch.feature.onboarding.management.addOnboardingStartScreen
import com.teampatch.feature.onboarding.permission.navigateToPermissionScreen
import com.teampatch.feature.onboarding.management.navigateToStartScreen
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

@Composable
fun MainNavHost(
    mainUiState: MainUiState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (mainUiState.isFirstUser) {
            OnboardingRoute
        } else if (!mainUiState.isOnboardingComplete) {
            OnboardingStartRoute
        } else {
            HomeRoute
        }
    ) {
        /** 온보딩 */

        addOnboardingScreen(
            onHomeScreenRequest = {
                navController.navigateToHomeScreen(
                    navOptions = navOptions {
                        popUpTo(OnboardingRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                )
            },
            onPermissionNotificationRequest = { navController.navigateToPermissionScreen() },
            onStartScreenRequest = { navController.navigateToStartScreen() }
        )

        addOnboardingPermissionScreen(
            onNextPageRequest = { navController.navigateToStartScreen() }
        )

        addOnboardingStartScreen(
            onBackRequest = navController::navigateUp,
            onboardingMakeGroupRequest = { navController.navigateToMakeGroupScreen() },
            onboardingEnterScreenRequest = { navController.navigateToEnterInvitationCodeScreen() }
        )

        /** 온보딩-Make */

        addOnboardingMakeParentsNameScreen(
            onBackRequest = navController::navigateUp,
            onShareInvitationScreenRequest = navController::navigateToMakeRelationScreen
        )

        addOnboardingMakeRelationScreen(
            onBackRequest = navController::navigateUp,
            onProfileSettingsScreenRequest = navController::navigateToMakeProfileSettingsScreen
        )

        addOnboardingMakeProfileSettingsScreen(
            onBackRequest = navController::navigateUp,
            onHomeRouteRequest = { navController.navigateToShareInvitationScreen() }
        )

        addOnboardingMakeInviteGrandParentsScreen(
            onBackRequest = navController::navigateUp,
            onHomeRouteRequest = { navController.navigateToHomeScreen() }
        )

        /** 온보딩-Enter */

        addOnboardingEnterInvitationCodeScreen(
            onBackRequest = navController::navigateUp,
            onEnterRelationScreenRequest = navController::navigateToEnterRelationScreen
        )

        addOnboardingEnterRelationScreen(
            onBackRequest = navController::navigateUp,
            onEnterProfileSettingsScreenRequest = navController::navigateToEnterProfileSettingsScreen
        )

        addOnboardingEnterProfileSettingsScreen(
            onBackRequest = navController::navigateUp,
            onEnterSpaceScreenRequest = { uris: List<Uri> ->
                // uris는 List<Uri> 타입
                val uriStrings = uris.map { it.toString() } // List<Uri> -> List<String>
                navController.navigateToEnterSpaceScreen(urisAsStrings = uriStrings) // 수정된 함수 호출
            }
        )

        addOnboardingEnterSpaceScreen(
            onBackRequest = navController::navigateUp,
            onHomeRouteRequest = { navController.navigateToHomeScreen() }
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