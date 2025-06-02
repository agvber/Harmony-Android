package com.teampatch.feature.onboarding.enter

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingEnterInvitationCodeRoute

fun NavController.navigateToEnterInvitationCodeScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingEnterInvitationCodeRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingEnterInvitationCodeScreen(
    onBackRequest: () -> Unit,
    onEnterRelationScreenRequest: () -> Unit,
) {
    composable<OnboardingEnterInvitationCodeRoute> {
    }
}

@Serializable
data object OnboardingEnterRelationRoute

fun NavController.navigateToEnterRelationScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingEnterRelationRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addOnboardingEnterRelationScreen(
    onBackRequest: () -> Unit,
    onEnterProfileSettingsScreenRequest: () -> Unit,
) {
    composable<OnboardingEnterRelationRoute> {
    }
}

@Serializable
data object OnboardingEnterProfileSettingsRoute // 기존과 동일

fun NavController.navigateToEnterProfileSettingsScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(OnboardingEnterProfileSettingsRoute, navOptions, navigatorExtras)
}

// OnboardingEnterSpaceRoute 수정: data object -> data class
@Serializable
data class OnboardingEnterSpaceRoute(
    val profileImageUrisAsStrings: List<String>, // Uri 문자열 리스트를 저장할 프로퍼티
)

// navigateToEnterSpaceScreen 함수 시그니처 및 호출 방식 수정
fun NavController.navigateToEnterSpaceScreen(
    urisAsStrings: List<String>, // List<String>을 파라미터로 받도록 변경
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    // 수정된 Route 객체를 생성하여 navigate 호출
    navigate(
        OnboardingEnterSpaceRoute(profileImageUrisAsStrings = urisAsStrings),
        navOptions,
        navigatorExtras
    )
}

// NavGraphBuilder 확장 함수들은 시그니처 변경 없이 내부 로직은 그대로 유지될 수 있습니다.
// 타입 추론에 의해 composable<T>의 T가 data class로 변경됩니다.

fun NavGraphBuilder.addOnboardingEnterProfileSettingsScreen(
    onBackRequest: () -> Unit,
    onEnterSpaceScreenRequest: (List<Uri>) -> Unit, // 이 콜백은 List<Uri>를 전달
) {
    composable<OnboardingEnterProfileSettingsRoute> {
    }
}

fun NavGraphBuilder.addOnboardingEnterSpaceScreen(
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
) {
    composable<OnboardingEnterSpaceRoute> {
    }
}