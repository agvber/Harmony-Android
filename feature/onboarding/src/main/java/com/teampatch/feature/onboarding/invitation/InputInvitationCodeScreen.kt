package com.teampatch.feature.onboarding.invitation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.feature.onboarding.R
import com.teampatch.feature.onboarding.common.OnboardingUiStateHelper
import com.teampatch.feature.onboarding.common.model.OnboardingAction
import com.teampatch.feature.onboarding.invitation.model.InputInvitationCodeEvent
import com.teampatch.feature.onboarding.invitation.model.InputInvitationCodeUiState

private const val MAX_LENGTH = 5

@Composable
internal fun InputInvitationCodeWithViewModel(
    onBackRequest: () -> Unit,
    onNextPageRequest: () -> Unit,
    viewModel: InputInvitationCodeViewModel = hiltViewModel(),
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current
    val uiState: InputInvitationCodeUiState by viewModel.uiState.collectAsStateWithLifecycle()

    InputInvitationCodeScreen(
        onBackRequest = onBackRequest,
        onNextPageRequest = viewModel::confirmInviteCode,
        onInviteCodeChange = viewModel::updateInviteCode,
        uiState = uiState
    )

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is InputInvitationCodeEvent.Error -> {
                        Toast.makeText(context, "초대 코드가 올바르지 않습니다.", Toast.LENGTH_LONG).show()
                    }

                    InputInvitationCodeEvent.Success -> {
                        val onboardingUiStateHelper = OnboardingUiStateHelper.getInstance()
                        onboardingUiStateHelper.updateAction(OnboardingAction.JOIN)
                        onNextPageRequest()
                    }
                }
            }
    }
}

@Composable
internal fun InputInvitationCodeScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: () -> Unit,
    onInviteCodeChange: (String) -> Unit,
    uiState: InputInvitationCodeUiState,
) {
    val focusManager = LocalFocusManager.current

    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MainGreen)) {
                append("초대코드")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("를\n입력해 주세요.")
            }
        },
        subtext = stringResource(R.string.subtext_onboarding_enter_invitation),
        onBackRequest = { onBackRequest() },
        bottomBar = {
            DefaultButton(
                onClick = { onNextPageRequest() },
                enabled = !uiState.isProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(stringResource(R.string.text_onboarding_enter_next))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            // 초대 코드 입력 필드
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(MAX_LENGTH) { index ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .background(Color(0xFFF7F7F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = uiState.inviteCode.getOrNull(index)?.toString() ?: "",
                            onValueChange = { value ->
                                if (value.length <= 1) {
                                    val newCode = StringBuilder(uiState.inviteCode).apply {
                                        if (index < length) {
                                            setCharAt(index, value.singleOrNull() ?: ' ')
                                        } else {
                                            append(value)
                                        }
                                    }.toString().trim()

                                    onInviteCodeChange(newCode)

                                    if (value.isNotEmpty() && index < MAX_LENGTH - 1) {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    }
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun InputInvitationCodeScreenPreview() {
    HarmonyTheme {
        InputInvitationCodeScreen(
            onBackRequest = {},
            onNextPageRequest = {},
            onInviteCodeChange = {},
            uiState = InputInvitationCodeUiState()
        )
    }
}