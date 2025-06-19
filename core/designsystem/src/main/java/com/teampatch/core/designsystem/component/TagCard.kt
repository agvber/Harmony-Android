package com.teampatch.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.Green2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen

@Composable
fun TagCard(
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Green2,
    shape: Shape = RoundedCornerShape(999.dp),
    innerPaddingValues: PaddingValues = PaddingValues(vertical = 2.dp, horizontal = 12.dp),
    propagateMinConstraints: Boolean = false
) {
    Box(
        propagateMinConstraints = propagateMinConstraints,
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(innerPaddingValues)
            .then(modifier),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.merge(
                color = MainGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun TagCardPreview() {
    HarmonyTheme {
        Row {
            listOf("손녀 조다은", "태어난 날", "울산 병원").forEach {
                TagCard(content = { Text(text = it) })
            }
        }
    }
}