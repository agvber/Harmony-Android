package com.teampatch.feature.onboarding.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.teampatch.core.designsystem.R.drawable.ic_camera_profile
import com.teampatch.core.designsystem.R.drawable.ic_my_appbar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.R
import com.teampatch.feature.onboarding.common.OnboardingUiStateHelper

private const val TAG: String = "OnboardingProfileSettingsScreen"
private val pickVisualMediaRequest: PickVisualMediaRequest =
    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

@Composable
internal fun ProfileSettingsScreen(
    onBackRequest: () -> Unit,
    onNextPageRequest: (uri: Uri) -> Unit,
) {
    val context: Context? = LocalContext.current
    var selectedProfileUri: Uri by remember { mutableStateOf(Uri.EMPTY) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                selectedProfileUri = it
                Log.d(TAG, "Picked URI: $it")
            }
                ?: Log.e(TAG, "No URI picked")
        }
    )

    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = BL)) {
                append("마지막으로\n")
            }
            withStyle(style = SpanStyle(color = MainGreen)) {
                append("프로필 사진")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("을 설정해요.")
            }
        },
        subtext = stringResource(R.string.subtext_onboarding_enter_name),
        onBackRequest = { onBackRequest() },
        bottomBar = {
            DefaultButton(
                onClick = {
                    OnboardingUiStateHelper.getInstance()
                        .updateProfileImage(selectedProfileUri)
                    onNextPageRequest(selectedProfileUri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(stringResource(R.string.text_onboarding_enter_enter_space))
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp)
                .noRippleClickable {
                    photoPicker.launch(pickVisualMediaRequest)
                }
        ) {
            Box(
                modifier = Modifier.size(144.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = selectedProfileUri.takeIf { it == Uri.EMPTY } ?: ic_my_appbar,
                        placeholder = painterResource(ic_my_appbar),
                        error = painterResource(ic_my_appbar)
                    ),
                    contentDescription = "profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)
                )

                // 카메라 아이콘을 이미지의 오른쪽 아래에 정렬
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .background(MainGreen, CircleShape)
                        .align(Alignment.BottomEnd) // ✅ 이미지 기준으로 오른쪽 아래 정렬
                ) {
                    Icon(
                        painter = painterResource(ic_camera_profile),
                        contentDescription = "camera",
                        tint = WH
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileSettingsScreenPreview() {
    HarmonyTheme {
        ProfileSettingsScreen(
            onBackRequest = {},
            onNextPageRequest = {}
        )
    }
}