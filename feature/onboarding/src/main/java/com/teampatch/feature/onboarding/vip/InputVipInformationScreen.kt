package com.teampatch.feature.onboarding.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.feature.onboarding.common.OnboardingUiStateHelper
import com.teampatch.feature.onboarding.vip.model.InputVipInformationUiState
import com.teampatch.feature.onboarding.vip.model.VipAlias

@Composable
internal fun InputVipInformationScreenWithViewModel(
    onBackRequest: () -> Unit,
    onNextPageRequest: (alias: String, name: String) -> Unit,
    viewModel: InputVipInformationViewModel = hiltViewModel(),
) {
    val uiState: InputVipInformationUiState by viewModel.uiState.collectAsStateWithLifecycle()

    InputVipInformationScreen(
        onBackRequest = onBackRequest,
        onComplete = {
            with(uiState) {
                OnboardingUiStateHelper.getInstance()
                    .updateVipInformation(vipName = vipName, vipAlias = vipAlias.toString())
                onNextPageRequest(vipAlias.toStringResource(), vipName)
            }
        },
        onVipNameChange = viewModel::updateVipName,
        onVipAliasChange = viewModel::updateVipAlias,
        uiState = uiState
    )
}

@Composable
internal fun InputVipInformationScreen(
    onBackRequest: () -> Unit,
    onComplete: () -> Unit,
    onVipNameChange: (String) -> Unit,
    onVipAliasChange: (VipAlias) -> Unit,
    uiState: InputVipInformationUiState,
) {
    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MainGreen)) {
                append("먼저")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("가족 공간")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("을\n만들어 주세요.")
            }
        },
        subtext = "할머니나 할아버지의 성함을\n입력해 주세요.",
        onBackRequest = onBackRequest,
        bottomBar = {
            DefaultButton(
                onClick = onComplete,
                enabled = uiState.isVipInformationValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text("다음")
            }
        }
    ) {
        CustomDropdownAndTextField(
            onVipAliasChange = onVipAliasChange,
            onNameChange = onVipNameChange,
            vipAlias = uiState.vipAlias,
            vipName = uiState.vipName
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDropdownAndTextField(
    onVipAliasChange: (VipAlias) -> Unit,
    onNameChange: (String) -> Unit,
    vipAlias: VipAlias,
    vipName: String,
) {
    var expanded by remember { mutableStateOf(false) }
    val vipAliasEntries = remember { VipAlias.entries }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .weight(1f)
        ) {
            OutlinedTextField(
                value = vipAlias.toStringResource(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = MainGreen
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vipAliasEntries.forEach { alias ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = vipAlias.toStringResource()
                            )
                        },
                        onClick = {
                            onVipAliasChange(alias)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = vipName,
            onValueChange = { onNameChange(it) },
            enabled = true,
            placeholder = { Text("", color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .background(G1, RoundedCornerShape(10.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Gray,
                disabledBorderColor = Color.LightGray,
                disabledContainerColor = Color(0xFFF5F5F5) // 배경색 설정
            )
        )
    }
}

internal fun VipAlias.toStringResource(): String = when (this) {
    VipAlias.GRAND_FATHER -> "할아버지"
    VipAlias.GRAND_MOTHER -> "할머니"
}

@Preview
@Composable
private fun InputVipInformationScreenPreview() {
    HarmonyTheme {
        InputVipInformationScreen(
            onBackRequest = {},
            onComplete = {},
            onVipNameChange = {},
            onVipAliasChange = {},
            uiState = InputVipInformationUiState()
        )
    }
}