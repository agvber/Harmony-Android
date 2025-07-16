package com.teampatch.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.R
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable

enum class NavigationItem {
    HOME,
    STORE,
    QUESTION,
    DAILY,
}

@Composable
fun DefaultBottomNavigation(
    onClick: (NavigationItem) -> Unit,
    navigationItem: NavigationItem,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 74.dp)
    ) {
        DefaultBottomNavigationIcon(
            onClick = { onClick(NavigationItem.HOME) },
            title = "홈",
            painter = painterResource(R.drawable.ic_home_navigation),
            isSelected = NavigationItem.HOME == navigationItem
        )
        DefaultBottomNavigationIcon(
            onClick = { onClick(NavigationItem.STORE) },
            title = "추억 보관소",
            painter = painterResource(R.drawable.ic_store_navigation),
            isSelected = NavigationItem.STORE == navigationItem
        )
        DefaultBottomNavigationIcon(
            onClick = { onClick(NavigationItem.QUESTION) },
            title = "질문",
            painter = painterResource(R.drawable.ic_question_navigation),
            isSelected = NavigationItem.QUESTION == navigationItem
        )
        DefaultBottomNavigationIcon(
            onClick = { onClick(NavigationItem.DAILY) },
            title = "일과",
            painter = painterResource(R.drawable.ic_calendar_navigation),
            isSelected = NavigationItem.DAILY == navigationItem
        )
    }
}

@Composable
private fun DefaultBottomNavigationIcon(
    onClick: () -> Unit,
    title: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val navigationIconColor by animateColorAsState(
        targetValue = if (isSelected) MainGreen else G3,
        label = "navigation_icon"
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .widthIn(min = 60.dp)
            .heightIn(min = 64.dp)
            .noRippleClickable(onClick = onClick)
    ) {
        Icon(
            painter = painter,
            contentDescription = "navigation icon",
            tint = navigationIconColor
        )
        Text(
            text = title,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = navigationIconColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultBottomNavigationIconPreview() {
    HarmonyTheme {
        DefaultBottomNavigationIcon(
            onClick = {},
            title = "홈",
            painter = painterResource(R.drawable.ic_home_navigation)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultBottomNavigationPreview() {
    HarmonyTheme {
        var navigationItem by remember { mutableStateOf(NavigationItem.HOME) }

        DefaultBottomNavigation(
            onClick = { navigationItem = it },
            navigationItem = navigationItem
        )
    }
}