package com.teampatch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.MeetMeFontFamily
import com.teampatch.core.designsystem.theme.PretendardFontFamily

@Composable
fun ColumnScope.CollapseMemoryCard(
    title: AnnotatedString,
    text: String,
    writer: String,
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
        modifier = Modifier.padding(top = 4.dp)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_letter_memory_card),
            contentDescription = "letter",
            modifier = Modifier
                .width(304.dp)
                .height(184.dp)
        )
        Text(
            text = writer,
            fontFamily = MeetMeFontFamily,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Preview
@Composable
private fun CollapseMemoryCardPreview() {
    HarmonyTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(G1)
                .padding(top = 28.dp, bottom = 24.dp)
        ) {
            CollapseMemoryCard(
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
                text = "어떤 추억인지 확인해 볼까요?",
                writer = "손녀 조다은"
            )
        }
    }
}