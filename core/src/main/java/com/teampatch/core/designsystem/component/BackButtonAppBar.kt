package com.teampatch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.R
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable

@Composable
fun BackButtonAppBar(
    onBackRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    AppBar(
        navigation = {
            Image(
                painter = painterResource(R.drawable.ic_back_appbar),
                contentDescription = "back",
                modifier = Modifier
                    .padding(start = 24.dp)
                    .noRippleClickable(onClick = onBackRequest)
            )
        },
        title = {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.merge(
                    TextStyle(
                        fontSize = 20.sp,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = BL
                    )
                )
            ) {
                title()
            }
        },
        actions = { actions() },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun BackButtonAppBarPreview() {
    HarmonyTheme {
        BackButtonAppBar(
            onBackRequest = { },
            title = {
                Text(text = "가족 정보")
            },
            actions = {
                Image(
                    painter = painterResource(R.drawable.ic_settings_appbar),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        )
    }
}