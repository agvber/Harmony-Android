package com.teampatch.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.R
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable

@Composable
fun ColumnScope.AdditionMemoryCard(
    onClick: () -> Unit,
    title: AnnotatedString,
    text: String,
) {
    Text(
        text = title,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp
    )
    Text(
        text = text,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        color = G4,
        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(304.dp)
            .height(184.dp)
            .background(G2, RoundedCornerShape(10.dp))
            .noRippleClickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_add_memory_card),
            contentDescription = "add"
        )
        Canvas(Modifier.fillMaxSize()) {
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            drawRoundRect(
                color = G3,
                style = Stroke(width = 2.dp.toPx(), pathEffect = pathEffect, cap = StrokeCap.Round),
                cornerRadius = CornerRadius(x = 10.dp.toPx())
            )
            BorderStroke(width = 2.dp, color = G3)
        }
    }
}

@Preview
@Composable
private fun AdditionMemoryCardPreview() {
    HarmonyTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(G1)
                .padding(top = 28.dp, bottom = 24.dp)
        ) {
            AdditionMemoryCard(
                onClick = {},
                title = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = BL)) {
                        append("오늘의 ")
                    }
                    withStyle(style = SpanStyle(color = MainGreen)) {
                        append("추억카드")
                    }
                    withStyle(style = SpanStyle(color = BL)) {
                        append("가 도착했어요")
                    }
                },
                text = "어떤 추억인지 확인해 볼까요?"
            )
        }
    }
}