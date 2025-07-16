package com.teampatch.feature.settings.group

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.teampatch.core.R.drawable.ic_export_family_info
import com.teampatch.core.R.drawable.ic_my_appbar
import com.teampatch.core.R.drawable.ic_settings_appbar
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.FamilyProfile
import com.teampatch.core.designsystem.component.FamilyRole
import com.teampatch.core.designsystem.component.RoundButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.designsystem.utils.previewPlaceholder
import com.teampatch.core.domain.fake.FakeFamilyInfo
import com.teampatch.core.domain.fake.FakeUserModel
import com.teampatch.core.domain.model.group.FamilyInfo
import com.teampatch.core.domain.model.user.Role
import com.teampatch.feature.R
import com.teampatch.feature.settings.group.model.SettingsGroupEvent
import com.teampatch.feature.settings.group.model.SettingsGroupUiState

@Composable
internal fun SettingsGroupScreenWithViewModel(
    onBackRequest: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileEditClick: () -> Unit,
    settingsGroupViewModel: SettingsGroupViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val familyInfoUiState by settingsGroupViewModel.familyInfoUiState.collectAsStateWithLifecycle()

    if (!familyInfoUiState.isLoading) {
        SettingsGroupScreen(
            onBackRequest = onBackRequest,
            onInviteClick = settingsGroupViewModel::inviteFamily,
            onSettingsClick = onSettingsClick,
            onProfileEditClick = onProfileEditClick,
            settingsGroupUiState = familyInfoUiState
        )
    }

    LaunchedEffect(Unit) {
        settingsGroupViewModel.event.collect { sideEffect ->
            when (sideEffect) {
                is SettingsGroupEvent.InviteError -> {
                    Toast.makeText(context, "초대 도중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }

                is SettingsGroupEvent.LoadError -> {
                    Toast.makeText(context, "정보를 불러오는 도중에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }

                is SettingsGroupEvent.Invite -> {
                    val shareIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, sideEffect.inviteCode)
                        type = "text/*"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, null))
                }
            }
        }
    }
}

@Composable
internal fun SettingsGroupScreen(
    onBackRequest: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileEditClick: () -> Unit,
    onInviteClick: () -> Unit,
    settingsGroupUiState: SettingsGroupUiState,
) {
    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = {
                    Text(
                        text = stringResource(R.string.text_group_appbar),
                        fontSize = 20.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = BL
                    )
                },
                actions = {
                    Image(
                        painter = painterResource(ic_settings_appbar),
                        contentDescription = "settings",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .noRippleClickable(onClick = onSettingsClick)
                    )
                }
            )
        }
    ) { scaffoldPaddingValues ->
        Box(
            modifier = Modifier
                .padding(scaffoldPaddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(G1)
                            .padding(vertical = 16.dp, horizontal = 20.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 1.dp, color = G2, shape = RoundedCornerShape(10.dp))
                                .background(color = WH, shape = RoundedCornerShape(10.dp))
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = settingsGroupUiState.user.profileImageUrl
                                        ?: ic_my_appbar,
                                    placeholder = previewPlaceholder(ic_my_appbar)
                                ),
                                contentDescription = "profile",
                                modifier = Modifier
                                    .size(72.dp)
                                    .padding(top = 20.dp)
                            )
                            Text(
                                text = settingsGroupUiState.user.name,
                                fontSize = 24.sp,
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = BL,
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 14.dp)
                            )
                            RoundButton(
                                onClick = { onProfileEditClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 42.dp)
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_edit_profile),
                                    fontSize = 18.sp,
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WH
                                )
                            }
                        }
                    }
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BL)) {
                                append("${stringResource(R.string.text_title_family)} ")
                            }
                            withStyle(style = SpanStyle(color = MainGreen)) {
                                append(settingsGroupUiState.familyInfo.size.toString())
                            }
                        },
                        fontSize = 22.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = BL,
                        modifier = Modifier
                            .padding(top = 36.dp, bottom = 24.dp, start = 20.dp)
                    )
                }

                items(settingsGroupUiState.familyInfo) { family ->
                    FamilyProfile(
                        profileImage = rememberAsyncImagePainter(
                            model = family.profileImageUrl ?: ic_my_appbar,
                            placeholder = previewPlaceholder(ic_my_appbar),
                            error = painterResource(ic_my_appbar)
                        ),
                        title = family.title,
                        name = family.name,
                        role = getFamilyRole(family),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                    )
                }

                item {
                    Box(modifier = Modifier.height(80.dp))
                }
            }

            var inviteButtonThrottleTime: Long = remember { 0L }

            DefaultButton(
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - inviteButtonThrottleTime >= 1000L) {
                        onInviteClick()
                        inviteButtonThrottleTime = currentTime
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Image(
                    painter = painterResource(ic_export_family_info),
                    contentDescription = "invite"
                )
                Text(
                    text = stringResource(R.string.btn_invite_family),
                    modifier = Modifier.padding(start = 18.dp)
                )
            }
        }
    }
}

private fun getFamilyRole(
    family: FamilyInfo,
): FamilyRole? = if (family.isManager) {
    FamilyRole.MANAGER
} else if (family.role == Role.VIP) {
    FamilyRole.ADMIN
} else {
    null
}

@Preview
@Composable
private fun SettingsGroupScreenPreview() {
    HarmonyTheme {
        SettingsGroupScreen(
            onBackRequest = {},
            onInviteClick = {},
            onSettingsClick = {},
            onProfileEditClick = {},
            settingsGroupUiState = SettingsGroupUiState(
                user = FakeUserModel().get().first(),
                familyInfo = FakeFamilyInfo().get()
            )
        )
    }
}