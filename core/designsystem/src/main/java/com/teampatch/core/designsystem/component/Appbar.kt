package com.teampatch.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    divider: Boolean = false,
    navigation: @Composable (BoxScope.() -> Unit) = {},
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .then(modifier)
    ) {
        navigation()

        Box(modifier = Modifier.align(Alignment.Center)) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.merge(
                    color = BL,
                    fontSize = 20.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                content = title
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )

        if (divider) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                thickness = 1.dp,
                color = G3,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppbarPreview() {
    HarmonyTheme {
        AppBar(
            title = { Text("설정") }
        )
    }
}