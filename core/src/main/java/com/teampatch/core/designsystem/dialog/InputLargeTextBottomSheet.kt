package com.teampatch.core.designsystem.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.R
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable

@Composable
fun ColumnScope.InputLargeTextBottomSheetContent(
    onDismissRequest: () -> Unit,
    onCompleteRequest: () -> Unit,
    buttonEnable: Boolean = true,
    title: @Composable () -> Unit,
    buttonText: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
    ) {
        CompositionLocalProvider(
            value = LocalTextStyle provides LocalTextStyle.current.merge(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = BL
            ),
            content = title
        )
        Image(
            painter = painterResource(R.drawable.ic_close_memory_card),
            contentDescription = "close",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)
                .noRippleClickable(onClick = onDismissRequest)
        )
    }

    content()

    DefaultButton(
        onClick = onCompleteRequest,
        enabled = buttonEnable,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.merge(
                fontSize = 20.sp
            )
        ) {
            buttonText()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InputLargeTextBottomSheetPreview() {
    HarmonyTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(bottom = 14.dp)
        ) {
            InputLargeTextBottomSheetContent(
                onDismissRequest = {},
                onCompleteRequest = {},
                title = { Text(text = "댓글 남기기") },
                buttonText = { Text(text = "작성 완료") }
            ) {
                DefaultTextField(
                    value = "",
                    onValueChange = {},
                    maxLines = 8,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp)
                        .height(204.dp)
                )
            }
        }
    }
}