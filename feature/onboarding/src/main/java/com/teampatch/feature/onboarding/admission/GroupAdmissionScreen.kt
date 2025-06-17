package com.teampatch.feature.onboarding.admission

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.common.findActivity
import com.teampatch.core.designsystem.R.drawable.ic_my_appbar
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.SpeechBubble
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.feature.onboarding.R
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
            with(uiStateHelper.uiState.value) {
                when (action) {
                    OnboardingAction.JOIN -> viewModel.joinGroup(
                        inviteCode = inviteCode,
                        memberName = managerName,
                        vipRelation = managerRelation,
                        memberProfileImageUri = profileImageUri
                    )

                    OnboardingAction.CREATE ->
                        viewModel.createGroup(
                            vipName = vipName,
                            vipAlias = vipAlias,
                            managerName = managerName,
                            managerRelation = managerRelation,
                            profileImageUri = profileImageUri
                        )

                    OnboardingAction.INIT -> context?.handleInitLoadError()
                }
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
            OnboardingAction.INIT -> context?.handleInitLoadError()
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
                            context?.getString(R.string.toast_admission_group_create_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    GroupAdmissionEvent.GroupJoinError -> {
                        Toast.makeText(
                            context,
                            context?.getString(R.string.toast_admission_group_join_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    GroupAdmissionEvent.Success -> onHomeRouteRequest()
                    GroupAdmissionEvent.GroupInformationLoadError -> Toast.makeText(
                        context,
                        context?.getString(R.string.toast_admission_group_information_load_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}

private fun Context.handleInitLoadError() {
    Toast.makeText(this, getString(R.string.toast_admission_init_load_error), Toast.LENGTH_LONG)
        .show()
    findActivity()?.recreate()
}

@Composable
private fun GroupAdmissionScreen(
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
    uiState: GroupAdmissionUiState,
) {
    Scaffold(
        topBar = { BackButtonAppBar(onBackRequest = onBackRequest) },
        bottomBar = {
            Column {
                SpeechBubble(
                    backgroundColor = WH,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_admission_speech_bubble),
                        color = G5,
                        fontSize = 18.sp,
                        fontFamily = PretendardFontFamily,
                        textAlign = TextAlign.Center,
                    )
                }
                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 32.dp, bottom = 8.dp),
                    onClick = onHomeRouteRequest,
                ) {
                    Text(text = stringResource(R.string.text_admission_bottom_button))
                }
            }
        }
    ) { scaffoldPaddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValue)
                .padding(top = 20.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = textSpanStyle.copy(color = MainGreen)) {
                        append(
                            stringResource(
                                R.string.text_admission_title1,
                                uiState.manager.run { "$vipRelation $name" }
                            )
                        )
                    }
                    withStyle(style = textSpanStyle.copy(color = BL)) {
                        append(stringResource(R.string.text_admission_title2))
                    }
                },
                lineHeight = 2.em,
                modifier = Modifier.padding(start = 20.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(ic_my_appbar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(144.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    uiState.members.forEach {
                        Image(
                            painter = painterResource(ic_my_appbar),
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                        )
                    }
                }
                Text(
                    text = stringResource(
                        R.string.text_admission_group_count,
                        uiState.memberSize
                    ),
                    fontSize = 18.sp,
                    color = G5,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

private val textSpanStyle = SpanStyle(
    color = BL,
    fontSize = 28.sp,
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold
)

@Preview(showBackground = true)
@Composable
private fun GroupAdmissionScreenPreview() {
    HarmonyTheme {
        GroupAdmissionScreen(
            onBackRequest = {},
            onHomeRouteRequest = {},
            uiState = GroupAdmissionUiState.init()
                .copy(
                    manager = GroupAdmissionUiState.Manager(
                        name = "손녀",
                        vipRelation = "조다은",
                        profileImageUri = null
                    ),
                    members = (0..1).map { GroupAdmissionUiState.Member(null) },
                )
                .let { it.copy(memberSize = it.members.size + 1) }
        )
    }
}