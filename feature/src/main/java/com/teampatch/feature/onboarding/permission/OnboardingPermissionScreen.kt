package com.teampatch.feature.onboarding.permission

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.R.drawable.ic_bell
import com.teampatch.core.R.drawable.img_character_fullbody_mony
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.SpeechBubble
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.feature.R

@SuppressLint("InlinedApi")
@Composable
fun OnboardingPermissionScreen(
    onNextPageRequest: () -> Unit,
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onNextPageRequest()
        }
    )

    Scaffold(
        bottomBar = {
            DefaultButton(
                onClick = {
                    permissionLauncher
                        .launch(Manifest.permission.POST_NOTIFICATIONS)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(text = stringResource(R.string.text_onboarding_start_harmony))
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 92.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpeechBubble(
                backgroundColor = WH,
                borderColor = G3
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = ic_bell),
                        contentDescription = null,
                        tint = MainGreen,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MainGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("알림")
                            }
                            append("을 허용해 주세요.")
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BL
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "하모니는 가족들이 보내는 알림을 통해\n진행되는 서비스예요.\n가족들과 함께 소중한 추억을 공유해봐요.",
                        fontSize = 14.sp,
                        color = G5,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Image(
                painter = painterResource(id = img_character_fullbody_mony),
                contentDescription = "Full-Body Mony Character",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPermissionScreenPreview() {
    HarmonyTheme {
        OnboardingPermissionScreen(onNextPageRequest = {})
    }
}