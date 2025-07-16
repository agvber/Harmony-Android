package com.teampatch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH

@Composable
fun ColumnScope.ExpandMemoryCard(
    title: String,
    description: String,
    painter: Painter,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = BL)) {
                append("${stringResource(R.string.expand_memory_card_title1)} ")
            }
            withStyle(style = SpanStyle(color = MainGreen)) {
                append(stringResource(R.string.expand_memory_card_title2))
            }
        },
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .padding(horizontal = 20.dp)
            .background(WH)
    ) {
        Image(
            painter = painter,
            contentDescription = "image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
        )
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                text = title,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = BL
            )
            Text(
                text = description,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = G5,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ExpandMemoryCardPreview() {
    HarmonyTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(G1)
                .padding(top = 28.dp, bottom = 24.dp)
        ) {
            ExpandMemoryCard(
                title = "다은이 태어난 날",
                description = "1999년 5월 4일",
                painter = painterResource(R.drawable.img_test_memory_card)
            )
        }
    }
}