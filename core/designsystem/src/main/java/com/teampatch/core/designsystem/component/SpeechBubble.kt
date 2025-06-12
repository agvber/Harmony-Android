package com.teampatch.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily

@Composable
fun SpeechBubble(
    modifier: Modifier = Modifier,
    backgroundColor: Color = G1,
    borderColor: Color = G3,
    strokeWidth: Dp = 2.dp,
    contentAlignment: Alignment = Alignment.Center,
    propagateMinConstraints: Boolean = false,
    content: @Composable (BoxScope.() -> Unit),
) {
    val density: Density = LocalDensity.current
    val stroke: Stroke by remember {
        mutableStateOf(with(density) { Stroke(strokeWidth.toPx()) })
    }

    Box(
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        modifier = Modifier
            .padding(top = 32.dp, start = 20.dp, end = 20.dp)
            .drawBehind {
                drawRoundRect(
                    color = backgroundColor,
                    size = size,
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = stroke
                )
                drawRoundRect(
                    color = borderColor,
                    size = size,
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = stroke
                )
                drawPath(
                    path = Path().apply {
                        moveTo(size.width / 2, size.height + 20.dp.toPx())
                        lineTo((size.width / 2) + 24.dp.toPx(), size.height - 20.dp.toPx())
                        lineTo((size.width / 2) - 24.dp.toPx(), size.height - 20.dp.toPx())
                        close()
                    },
                    color = backgroundColor
                )
                drawLine(
                    color = borderColor,
                    start = Offset(size.width / 2, size.height + 20.dp.toPx()),
                    end = Offset((size.width / 2) + 12.dp.toPx(), size.height),
                    strokeWidth = stroke.width
                )
                drawLine(
                    color = borderColor,
                    start = Offset(size.width / 2, size.height + 20.dp.toPx()),
                    end = Offset((size.width / 2) - 12.dp.toPx(), size.height),
                    strokeWidth = stroke.width
                )
            }
            .padding(horizontal = 20.dp)
            .then(modifier)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(
                color = BL,
                fontSize = 25.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium
            )
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeechBubblePreview() {
    HarmonyTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            SpeechBubble(
                modifier = Modifier.heightIn(min = 134.dp)
            ) {
                Text(text = "안녕하세요! 추억을 기록하러 오셨군요. 아래 버튼을 누르면 기록을 시작합니다.")
            }
        }
    }
}