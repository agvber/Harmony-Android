package com.teampatch.feature.onboarding.admission

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.teampatch.core.designsystem.R.drawable.ic_my_appbar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.OnBoardingLayout
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen

@Composable
internal fun GroupAdmissionRoute(
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
) {
    GroupAdmissionScreen(
        profileImageUris = listOf(),
        onBackRequest = onBackRequest,
        onHomeRouteRequest = onHomeRouteRequest
    )
}

@Composable
fun GroupAdmissionScreen(
    profileImageUris: List<Uri>,
    onBackRequest: () -> Unit,
    onHomeRouteRequest: () -> Unit,
) {
    OnBoardingLayout(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MainGreen)) {
                append("a")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("b")
            }
            withStyle(style = SpanStyle(color = BL)) {
                append("C")
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
                    model = painterResource(id = ic_my_appbar),
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
            profileImageUris = emptyList(),
            onBackRequest = {},
            onHomeRouteRequest = {}
        )
    }
}