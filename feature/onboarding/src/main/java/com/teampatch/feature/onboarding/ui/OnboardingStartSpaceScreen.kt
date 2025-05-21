package com.teampatch.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teampatch.core.designsystem.R.drawable.btn_enter_space_onboarding
import com.teampatch.core.designsystem.R.drawable.btn_make_space_onboarding
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.feature.onboarding.R

/**
 * 여기서 OnboardingStartRoute를 만들고
 * 파라미터로 request 2개, route 2개, 스크린도 2개(MakeGroup, Enter) 만들기
 */

@Composable
fun OnboardingStartSpaceScreen(
    onBackRequest: () -> Unit,
    onboardingMakeGroupRequest: () -> Unit,
    onboardingEnterScreenRequest: () -> Unit,
) {
    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = BL)) {
                append(stringArrayResource(R.array.title_onboarding_make_space)[0])
            }
            withStyle(style = SpanStyle(color = MainGreen)) {
                append(stringArrayResource(R.array.title_onboarding_make_space)[1])
            }
            withStyle(style = SpanStyle(color = BL)) {
                append(stringArrayResource(R.array.title_onboarding_make_space)[2])
            }
        },
        subtext = stringResource(R.string.subtext_onboarding_make_space),
        onBackRequest = { onBackRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White) // 배경색 설정
        ) {
            Image(
                painter = painterResource(btn_make_space_onboarding),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onboardingMakeGroupRequest() }
            )

            // Spacer 대신 Box로 배경색을 설정한 여백 추가
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.White) // 여백의 배경색을 설정
            )

            Image(
                painter = painterResource(btn_enter_space_onboarding),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onboardingEnterScreenRequest() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingStartScreenPreview() {
    HarmonyTheme {
        OnboardingStartSpaceScreen(
            onBackRequest = {},
            onboardingMakeGroupRequest = {},
            onboardingEnterScreenRequest = {}
        )
    }
}