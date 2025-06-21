package com.teampatch.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable

@Composable
fun DefaultButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: DefaultButtonColor = DefaultButtonColor(),
    shape: Shape = RoundedCornerShape(10.dp),
    contentPaddingValues: PaddingValues = PaddingValues(),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .widthIn(min = 80.dp)
            .heightIn(min = 68.dp)
            .background(
                color = if (enabled) color.containerColor else color.disabledContainerColor,
                shape = shape
            )
            .padding(contentPaddingValues)
            .noRippleClickable(enabled = enabled, onClick = onClick)
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

data class DefaultButtonColor(
    val containerColor: Color = MainGreen,
    val disabledContainerColor: Color = G2,
)

@Preview
@Composable
private fun DefaultButtonPreview() {
    HarmonyTheme {
        DefaultButton(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("가족 초대하기")
        }
    }
}