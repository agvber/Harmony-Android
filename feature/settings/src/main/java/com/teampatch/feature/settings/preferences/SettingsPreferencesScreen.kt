package com.teampatch.feature.settings.preferences

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teampatch.core.common.startNotificationSettingsActivity
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.SubRed
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.settings.R
import com.teampatch.feature.settings.preferences.model.SettingsPreferencesEvent
import com.teampatch.feature.settings.preferences.model.SettingsPreferencesUiState

@Composable
internal fun SettingsPreferencesRoute(
    onBackRequest: () -> Unit,
    onExitAppRequest: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTosClick: () -> Unit, // Terms of service
    settingsPreferencesViewModel: SettingsPreferencesViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by settingsPreferencesViewModel.settingsPreferencesUiState.collectAsStateWithLifecycle()

    SettingsPreferencesScreen(
        onBackRequest = onBackRequest,
        onPrivacyPolicyClick = onPrivacyPolicyClick,
        onTosClick = onTosClick,
        onLogoutRequest = settingsPreferencesViewModel::logout,
        onWithdrawFamilyGroupRequest = settingsPreferencesViewModel::withdrawFamilyGroup,
        onWithdrawRequest = settingsPreferencesViewModel::withdrawApp,
        settingsPreferencesUiState = uiState
    )

    LaunchedEffect(Unit) {
        settingsPreferencesViewModel.event.collect { sideEffect ->
            when (sideEffect) {
                is SettingsPreferencesEvent.LoadError -> {
                    Toast.makeText(context, "앱 정보를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show()
                }

                is SettingsPreferencesEvent.LogoutError -> {
                    Toast.makeText(context, "로그아웃을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }

                is SettingsPreferencesEvent.LogoutSuccess -> {
                    onExitAppRequest()
                }

                is SettingsPreferencesEvent.WithdrawAppError -> {
                    Toast.makeText(context, "회원 탈퇴를 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }

                is SettingsPreferencesEvent.WithdrawAppSuccess -> {
                    onExitAppRequest()
                }

                is SettingsPreferencesEvent.WithdrawFamilyGroupError -> {
                    Toast.makeText(context, "가족 탈퇴를 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }

                is SettingsPreferencesEvent.WithdrawFamilyGroupSuccess -> {
                    onExitAppRequest()
                }
            }
        }
    }
}

@Composable
internal fun SettingsPreferencesScreen(
    onBackRequest: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTosClick: () -> Unit, // Terms of service
    onLogoutRequest: () -> Unit,
    onWithdrawFamilyGroupRequest: () -> Unit,
    onWithdrawRequest: () -> Unit,
    settingsPreferencesUiState: SettingsPreferencesUiState,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = {
                    Text(text = stringResource(R.string.text_title_appbar))
                }
            )
        }
    ) { scaffoldPaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .background(color = G1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = WH)
            ) {
                Text(
                    text = stringResource(R.string.text_permission_title),
                    color = G5,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SettingsItem(
                        onClick = {
                            context.startNotificationSettingsActivity("com.teampatch.harmony")
                        },
                        text = stringResource(R.string.text_notification_item)
                    )
                }
                SettingsItem(
                    onClick = { onPrivacyPolicyClick() },
                    text = stringResource(R.string.text_privacy_item)
                )
                SettingsItem(
                    onClick = { onTosClick() },
                    text = stringResource(R.string.text_tos_item)
                )

                HorizontalDivider(
                    thickness = 8.dp,
                    color = G1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Text(
                    text = stringResource(R.string.text_account_title),
                    color = G5,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
                )

                SettingsItem(
                    onClick = { onLogoutRequest() },
                    text = stringResource(R.string.text_logout_item),
                    color = SubRed
                )
                SettingsItem(
                    onClick = { onWithdrawFamilyGroupRequest() },
                    text = stringResource(R.string.text_withdraw_family_item)
                )
                SettingsItem(
                    onClick = { onWithdrawRequest() },
                    text = stringResource(R.string.text_withdraw_item),
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
            }

            if (!settingsPreferencesUiState.isLoading) {
                Text(
                    text = settingsPreferencesUiState.getVersionString(context),
                    color = G4,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

fun SettingsPreferencesUiState.getVersionString(context: Context): String = if (isLatestVersion) {
    "$installedVersion " +
        context.getString(R.string.text_version_latest)
} else {
    "$installedVersion " +
        context.getString(R.string.text_version_oldest)
}

@Composable
private fun SettingsItem(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BL,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(horizontal = 20.dp)
            .then(modifier)
            .noRippleClickable { onClick() }
    ) {
        Text(
            text = text,
            color = color,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun SettingsPreferencesScreenPreview() {
    HarmonyTheme {
        SettingsPreferencesScreen(
            onBackRequest = {},
            onPrivacyPolicyClick = {},
            onTosClick = {},
            onLogoutRequest = {},
            onWithdrawFamilyGroupRequest = {},
            onWithdrawRequest = {},
            settingsPreferencesUiState = SettingsPreferencesUiState(installedVersion = "1.0.0", isLoading = false)
        )
    }
}