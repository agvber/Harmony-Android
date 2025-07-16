package com.teampatch.feature.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.teampatch.core.R.drawable.ic_kakao_symbol
import com.teampatch.core.designsystem.theme.DP120
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP200
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP320
import com.teampatch.core.designsystem.theme.DP52
import com.teampatch.core.designsystem.theme.DP72
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.KakaoPrimary
import com.teampatch.core.designsystem.theme.RoundedCornerShape12
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.R

@Composable
internal fun KakaoLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .widthIn(min = DP320)
            .heightIn(min = DP72)
            .padding(horizontal = DP20, vertical = DP8)
            .background(color = KakaoPrimary, RoundedCornerShape12)
            .clip(RoundedCornerShape12)
            .padding(horizontal = DP16)
            .noRippleClickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(id = ic_kakao_symbol),
            contentDescription = stringResource(R.string.login_button_kakao_description),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(DP24)
        )
        Text(
            text = stringResource(R.string.login_button_kakao),
            fontSize = SP18,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun KakaoLoginButtonPreview() {
    HarmonyTheme {
        KakaoLoginButton(onClick = {})
    }
}