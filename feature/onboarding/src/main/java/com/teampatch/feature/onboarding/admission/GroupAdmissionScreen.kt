package com.teampatch.feature.onboarding.admission

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.teampatch.core.common.findActivity
import com.teampatch.core.designsystem.R.drawable.ic_my_appbar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.feature.onboarding.admission.model.GroupAdmissionEvent
import com.teampatch.feature.onboarding.admission.model.GroupAdmissionUiState
import com.teampatch.feature.onboarding.common.OnboardingUiStateHelper
import com.teampatch.feature.onboarding.common.model.OnboardingAction
import com.teampatch.feature.onboarding.common.model.OnboardingCommonUiState

@Composable
internal fun GroupAdmissionWithViewModel(
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
    viewModel: GroupAdmissionViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context: Context? = LocalContext.current

    val uiStateHelper: OnboardingUiStateHelper = remember {
        OnboardingUiStateHelper.getInstance()
    }

    val uiState: GroupAdmissionUiState by viewModel.uiState.collectAsStateWithLifecycle()

    GroupAdmissionScreen(
        onBackRequest = onBackRequest,
        onHomeRouteRequest = {
            val uiState: OnboardingCommonUiState = uiStateHelper.uiState.value

            when (uiState.action) {
                OnboardingAction.JOIN -> viewModel.joinGroup(uiState.inviteCode)
                OnboardingAction.CREATE ->
                    viewModel.createGroup(
                        vipName = uiState.vipName,
                        vipAlias = uiState.vipAlias,
                        managerName = uiState.managerName,
                        managerRelation = uiState.managerRelation,
                        profileImageUri = uiState.profileImageUri
                    )

                OnboardingAction.INIT -> context?.loadErrorMethod()
            }
        },
        uiState = uiState
    )

    SideEffect {
        val uiState: OnboardingCommonUiState = uiStateHelper.uiState.value
        when (uiState.action) {
            OnboardingAction.CREATE -> viewModel.loadDemoGroupInformation(
                managerName = uiState.managerName,
                managerRelation = uiState.managerRelation
            )

            OnboardingAction.JOIN -> viewModel.loadInvitedGroupInformation(uiState.inviteCode)
            OnboardingAction.INIT -> context?.loadErrorMethod()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    GroupAdmissionEvent.GroupCreateError -> {
                        Toast.makeText(
                            context,
                            "가족 공간 생성에 실패했습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    GroupAdmissionEvent.GroupJoinError -> {
                        Toast.makeText(
                            context,
                            "가족 공간을 찾을 수 없습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    GroupAdmissionEvent.Success -> onHomeRouteRequest()
                    GroupAdmissionEvent.GroupInformationLoadError -> Toast.makeText(
                        context,
                        "초대된 가족공간의 정보를 가져올 수 없습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}

private fun Context.loadErrorMethod() {
    Toast.makeText(this, "온보딩 정보가 존재하지 않습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG)
        .show()
    findActivity()?.recreate()
}

@Composable
private fun GroupAdmissionScreen(
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
    uiState: GroupAdmissionUiState,
) {
    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MainGreen)) {
                append("손녀 조다은님")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("이\n")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("만든 가족공간이에요.")
            }
        },
        subtext = "",
        onBackRequest = onBackRequest,
        bottomBar = {
            DefaultButton(
                onClick = { onHomeRouteRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text("3명의 구성원")
            }
        },
        image = {
            Image(
                painter = painterResource(ic_my_appbar),
                contentDescription = "Onboarding Illustration",
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        imagePadding = 15.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ic_my_appbar,
                    placeholder = painterResource(id = ic_my_appbar),
                    error = painterResource(id = ic_my_appbar)
                ),
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(144.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupAdmissionScreenPreview() {
    HarmonyTheme {
        GroupAdmissionScreen(
            onBackRequest = {},
            onHomeRouteRequest = {},
            uiState = GroupAdmissionUiState.init()
        )
    }
}