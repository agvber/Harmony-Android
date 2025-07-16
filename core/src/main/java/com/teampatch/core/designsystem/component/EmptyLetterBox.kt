package com.teampatch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily

@Composable
fun ColumnScope.EmptyLetterBox() {
    Image(
        painter = painterResource(R.drawable.ic_letter_box_memory_card),
        contentDescription = "letter box",
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = BL)) {
                append("${stringResource(R.string.empty_memory_card_title1)} ")
            }
            withStyle(style = SpanStyle(color = MainGreen)) {
                append(stringResource(R.string.empty_memory_card_title2))
            }
            withStyle(style = SpanStyle(color = BL)) {
                append(stringResource(R.string.empty_memory_card_title3))
            }
        },
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        modifier = Modifier
            .padding(top = 18.dp)
            .align(Alignment.CenterHorizontally)
    )
    Text(
        text = stringResource(R.string.empty_memory_card_description),
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        color = G4,
        modifier = Modifier
            .padding(top = 4.dp)
            .align(Alignment.CenterHorizontally)

    )
}

@Preview
@Composable
private fun EmptyLetterBoxPreview() {
    HarmonyTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(G1)
                .padding(top = 28.dp, bottom = 24.dp)
        ) {
            EmptyLetterBox()
        }
    }
}