package com.teampatch.feature.onboarding.relation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.feature.onboarding.OnboardingCommonUiState
import com.teampatch.feature.onboarding.R

@Composable
internal fun InputGroupRelationRoute(
    onBackRequest: () -> Unit,
    onEnterProfileSettingsScreenRequest: () -> Unit,
) {
    val context: Context = LocalContext.current
    val registry = LocalSaveableStateRegistry.current

    InputGroupRelationScreen(
        onBackRequest = onBackRequest,
        onResult = { relation: String, name: String ->
            val restore = registry?.consumeRestored(OnboardingCommonUiState.TAG)
            (restore as? OnboardingCommonUiState)?.let {
                registry.registerProvider(OnboardingCommonUiState.TAG) {
                    it.copy(managerAlias = relation, managerName = name)
                }
            }
        }
    )

//    LaunchedEffect(Unit) {
//        viewModel.onboardingInputInvitationCodeEvent
//            .flowWithLifecycle(lifecycleOwner.lifecycle)
//            .collectLatest {
//                when (it) {
//                    is OnboardingInputInvitationCodeEvent.Success -> onEnterProfileSettingsScreenRequest()
//                    is OnboardingInputInvitationCodeEvent.Error -> {
//                        Toast.makeText(
//                            context,
//                            "관계 설정 과정에서 에러가 발생하였습니다.\n다시 시도 해주세요.",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }
//    }
}

@Composable
internal fun InputGroupRelationScreen(
    onBackRequest: () -> Unit,
    onResult: (relation: String, name: String) -> Unit,
) {
    var relation by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }

    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = BL)) {
                append("할머니와")
            }
            withStyle(style = SpanStyle(color = MainGreen)) {
                append("어떤 관계")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("인가요?")
            }
        },
        subtext = stringResource(R.string.subtext_onboarding_enter_relation),
        onBackRequest = { onBackRequest() },
        bottomBar = {
            DefaultButton(
                onClick = { onResult(relation, name) },
                enabled = relation.isNotBlank() && name.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(stringResource(R.string.text_onboarding_enter_next))
            }
        }
    ) {
        CustomTextField(
            relation = relation,
            onRelationChange = { relation = it },
            name = name,
            onNameChange = { name = it }
        )
    }
}

@Composable
fun CustomTextField(
    relation: String,
    onRelationChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.text_onboarding_enter_relation_title),
                fontFamily = PretendardFontFamily,
                color = BL,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 9.dp)
            )

            OutlinedTextField(
                value = relation,
                onValueChange = { onRelationChange(it) },
                enabled = true,
                placeholder = {
                    Text(
                        stringResource(R.string.text_onboarding_enter_grandson),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(G1),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = G2
                )
            )
        }

        Column {
            Text(
                text = stringResource(R.string.text_onboarding_enter_name_title),
                fontFamily = PretendardFontFamily,
                color = BL,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 9.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { onNameChange(it) },
                enabled = true,
                placeholder = {
                    Text(
                        stringResource(R.string.text_onboarding_enter_name_placeholder),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(G1),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = G2

                )
            )
        }
    }
}

@Preview
@Composable
private fun InputGroupRelationScreenPreview() {
    HarmonyTheme {
        InputGroupRelationScreen(
            onBackRequest = {},
            onResult = { _, _ -> }
        )
    }
}