package com.teampatch.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.Green2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.RoundedCornerShape10

@Composable
fun FilterChip(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    color: FilterChipColor = FilterChipColor(),
    shape: Shape = RoundedCornerShape10,
    content: @Composable () -> Unit
) {
    val backgroundColor by animateColorAsState(if (isSelected) color.containerColor else color.disableContainerColor)
    val strokeColor by animateColorAsState(if (isSelected) color.strokeColor else color.disableStrokeColor)
    val contentColor by animateColorAsState(if (isSelected) color.contentColor else color.disableContentColor)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(backgroundColor, shape)
            .border(1.dp, strokeColor, shape)
            .clip(shape)
            .sizeIn(minWidth = 40.dp, minHeight = 48.dp)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.merge(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = contentColor
            ),
            content = content
        )
    }
}

data class FilterChipColor(
    val disableStrokeColor: Color = G2,
    val disableContentColor: Color = G4,
    val disableContainerColor: Color = G1,
    val strokeColor: Color = MainGreen,
    val contentColor: Color = MainGreen,
    val containerColor: Color = Green2,
)

@Preview(showBackground = true)
@Composable
private fun FilterChipPreview() {
    HarmonyTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(40.dp)
        ) {
            FilterChip(false) { Text("월") }
            FilterChip(true) { Text("화") }
        }
    }
}