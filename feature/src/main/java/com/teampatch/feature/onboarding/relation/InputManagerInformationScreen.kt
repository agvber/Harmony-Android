package com.teampatch.feature.onboarding.relation

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.feature.onboarding.common.ui.layout.OnboardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.feature.R
import com.teampatch.feature.onboarding.common.OnboardingUiStateHelper

@Composable
internal fun InputManagerInformationScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: (relation: String, name: String) -> Unit,
) {
    var relation: String by rememberSaveable { mutableStateOf("") }
    var name: String by rememberSaveable { mutableStateOf("") }

    val isBottomButtonEnabled: Boolean by remember {
        derivedStateOf { relation.isNotBlank() && name.isNotBlank() }
    }

    OnboardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = BL)) {
                append(stringResource(R.string.text_input_manager_information_title1))
            }
            withStyle(style = SpanStyle(color = MainGreen)) {
                append(stringResource(R.string.text_input_manager_information_title2))
            }
            withStyle(style = SpanStyle(color = BL)) {
                append(stringResource(R.string.text_input_manager_information_title3))
            }
        },
        subTitle = stringResource(R.string.subtext_onboarding_enter_relation),
        onBackRequest = onBackRequest,
        bottomBar = {
            DefaultButton(
                onClick = {
                    OnboardingUiStateHelper.getInstance()
                        .updateManagerInformation(name, relation)
                    onNextPageRequest(relation, name)
                },
                enabled = isBottomButtonEnabled,
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
private fun CustomTextField(
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
private fun InputManagerInformationScreenPreview() {
    HarmonyTheme {
        InputManagerInformationScreen(
            onBackRequest = {},
            onNextPageRequest = { _, _ -> }
        )
    }
}