package com.teampatch.feature.daily.management.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SubRed
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.management.model.DropDownOption

@Composable
internal fun DailyManagementDropDown(
    onDismissRequest: () -> Unit,
    onSelectItem: (DropDownOption) -> Unit,
    isDropDownMenuShow: Boolean,
    modifier: Modifier = Modifier.Companion,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
) {
    DropdownMenu(
        expanded = isDropDownMenuShow,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape10,
        modifier = modifier
            .widthIn(min = 200.dp),
        offset = offset
    ) {
        DropDownOption.entries.forEach { option ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.dropdown_edit_daily),
                        style = DropDownTextStyle.copy(
                            color = when (option) {
                                DropDownOption.EDIT -> BL
                                DropDownOption.DELETE -> SubRed
                            }
                        ),
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                    )
                },
                onClick = { onSelectItem(DropDownOption.EDIT) }
            )
        }
    }
}

private val DropDownTextStyle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    color = BL,
    lineHeight = 1.4.em,
    textAlign = TextAlign.Center,
)

@Preview(showSystemUi = true)
@Composable
private fun DailyManagementDropDownPreview() {
    HarmonyTheme {
        DailyManagementDropDown(onDismissRequest = {}, onSelectItem = {}, isDropDownMenuShow = true)
    }
}