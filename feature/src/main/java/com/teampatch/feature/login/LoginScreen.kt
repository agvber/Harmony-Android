package com.teampatch.feature.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.R.drawable.img_logo_in_login
import com.teampatch.core.designsystem.theme.DP136
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.feature.R
import com.teampatch.feature.login.component.KakaoLoginButton
import com.teampatch.feature.login.model.LoginEvent

@Composable
internal fun LoginWithViewModelScreen(
    onHomeScreenRequest: () -> Unit,
    onPermissionNotificationRequest: () -> Unit,
    onStartSpaceScreenRequest: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current

    LoginScreen(onKakaoLoginRequest = viewModel::loginKakao)

    LaunchedEffect(Unit) {
        viewModel.loginEvent
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is LoginEvent.Error -> {
                        Toast.makeText(
                            context,
                            R.string.login_toast_login_error_message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    LoginEvent.FamilyRegistrationRequired -> {
                        if (!hasNotificationGranted(context)) {
                            onPermissionNotificationRequest()
                            return@collect
                        }
                        onStartSpaceScreenRequest()
                    }

                    LoginEvent.Success -> {
                        onHomeScreenRequest()
                    }
                }
            }
    }
}

@Composable
internal fun LoginScreen(onKakaoLoginRequest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(G1)
            .padding(top = DP136, bottom = DP8),
    ) {
        Image(
            painter = painterResource(id = img_logo_in_login),
            contentDescription = stringResource(R.string.login_image_harmony_description),
            modifier = Modifier
                .fillMaxWidth()
        )
        KakaoLoginButton(
            onClick = onKakaoLoginRequest,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)

        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onKakaoLoginRequest = { }
    )
}