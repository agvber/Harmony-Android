package com.teampatch.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable

@Composable
fun RoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 48.dp),
    enable: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(MainGreen, RoundedCornerShape(999.dp))
            .noRippleClickable(
                enabled = enable,
                onClick = onClick
            )
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.merge(
                color = WH,
                fontSize = 24.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoundButtonPreview() {
    HarmonyTheme {
        RoundButton(
            onClick = {}
        ) {
            Text("Click me!")
        }
    }
}