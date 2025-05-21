package com.teampatch.feature.onboarding.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.teampatch.core.designsystem.R
import com.teampatch.feature.onboarding.model.LoginEvent

@Composable
internal fun OnboardingLoginScreen(
    onHomeScreenRequest: () -> Unit,
    onPermissionNotificationRequest: () -> Unit,
    onStartSpaceScreenRequest: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    fun hasNotificationGranted(context: Context): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect { event ->
            when (event) {
                is LoginEvent.Error -> {
                    Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
                LoginEvent.FamilyRegistrationRequired -> {
                    if (!hasNotificationGranted(context)) {
                        onPermissionNotificationRequest()
                    } else {
                        onStartSpaceScreenRequest()
                    }
                }
                LoginEvent.Success -> {
                    onHomeScreenRequest()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { viewModel.loginKakao() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kakao_login_medium_wide),
                    contentDescription = "Kakao Login",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(top = 135.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_in_login),
                contentDescription = "Logo Harmony",
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(465.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingLoginScreenPreview() {
    OnboardingLoginScreen(
        onHomeScreenRequest = {},
        onPermissionNotificationRequest = {},
        onStartSpaceScreenRequest = {}
    )
}