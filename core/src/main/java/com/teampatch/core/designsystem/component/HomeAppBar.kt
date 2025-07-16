package com.teampatch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teampatch.core.R
import com.teampatch.core.designsystem.theme.HarmonyTheme

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    AppBar(
        navigation = {
            Image(
                painter = painterResource(R.drawable.ic_harmony_appbar),
                contentDescription = "appbar_logo",
                modifier = Modifier.padding(start = 28.dp)
            )
        },
        title = { },
        actions = { actions() },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeAppBarPreview() {
    HarmonyTheme {
        HomeAppBar {
            Image(
                painter = painterResource(R.drawable.ic_my_appbar),
                contentDescription = "my",
                modifier = Modifier.padding(end = 20.dp)
            )
        }
    }
}