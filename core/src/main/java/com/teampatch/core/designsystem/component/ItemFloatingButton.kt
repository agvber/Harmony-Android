package com.teampatch.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen

@Composable
fun ItemFloatingButton(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawBehind {
                drawCircle(color = MainGreen)
            }
            .size(64.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemFloatingButtonPreview() {
    HarmonyTheme {
        ItemFloatingButton {
            Text("+")
        }
    }
}