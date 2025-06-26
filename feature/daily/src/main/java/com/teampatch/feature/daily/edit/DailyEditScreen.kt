package com.teampatch.feature.daily.edit

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_close_memory_card
import com.teampatch.core.designsystem.R.drawable.ic_time_text_field
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.component.FilterChip
import com.teampatch.core.designsystem.dialog.TimePickerDialog
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP32
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP52
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.LocalHarmonyTimPickerColors
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.edit.model.DailyEditEvent
import com.teampatch.feature.daily.edit.model.DailyEditMode
import com.teampatch.feature.daily.edit.model.DailyEditUiState
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun DailyEditScreenWithViewModel(
    onDismissRequest: () -> Unit,
    viewModel: DailyEditViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val uiState: DailyEditUiState by viewModel.uiState.collectAsStateWithLifecycle()
    DailyEditScreen(
        onDismissRequest = onDismissRequest,
        onCompleteRequest = viewModel::uploadDailyRoutine,
        onTitleChange = viewModel::changeTitleText,
        onDayOfWeekChange = viewModel::changeDayOfWeek,
        onTimeChange = viewModel::changeTime,
        uiState = uiState
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is DailyEditEvent.AddDailyError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_edit_data_send_error),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is DailyEditEvent.LoadError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_edit_load_error),
                        Toast.LENGTH_LONG
                    ).show()
                    onDismissRequest()
                }

                DailyEditEvent.DailyEditSuccess -> onDismissRequest()
                DailyEditEvent.TimeFormatError -> Toast.makeText(
                    context,
                    context.getString(R.string.toast_edit_time_format_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DailyEditScreen(
    onDismissRequest: () -> Unit,
    onCompleteRequest: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDayOfWeekChange: (DayOfWeek) -> Unit,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
    uiState: DailyEditUiState,
) {
    val context = LocalContext.current
    var isTimePickerDialogShow by remember { mutableStateOf(false) }
    val isBottomButtonEnabled by remember(uiState) { derivedStateOf { uiState.isNextButtonEnabled() } }
    val timePickerState = rememberTimePickerState(
        initialHour = uiState.time.hour,
        initialMinute = uiState.time.minute
    )
    if (isTimePickerDialogShow) {
        TimePickerDialog(
            onDismissRequest = { isTimePickerDialogShow = false },
            onCancel = {
                TextButton(onClick = { isTimePickerDialogShow = false }) {
                    Text(
                        text = stringResource(R.string.text_date_picker_cancel),
                        color = BL,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            onConfirm = {
                TextButton(
                    onClick = {
                        with(timePickerState) {
                            onTimeChange(hour, minute)
                        }
                        isTimePickerDialogShow = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.text_date_picker_confirm),
                        color = MainGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            title = stringResource(R.string.text_date_picker_title)
        ) {
            TimePicker(
                state = timePickerState,
                colors = LocalHarmonyTimPickerColors.current
            )
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = {
                    Text(
                        text = when (uiState.dailyEditMode) {
                            DailyEditMode.ADD -> stringResource(R.string.appbar_daily_add)
                            DailyEditMode.EDIT -> stringResource(R.string.appbar_daily_edit)
                        }
                    )
                },
                actions = {
                    Image(
                        painter = painterResource(ic_close_memory_card),
                        contentDescription = stringResource(R.string.image_exit_description),
                        modifier = Modifier
                            .padding(end = DP20)
                            .noRippleClickable(onClick = onDismissRequest)
                    )
                }
            )
        },
        bottomBar = {
            DefaultButton(
                onClick = onCompleteRequest,
                enabled = isBottomButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
                    .padding(bottom = DP8)
            ) {
                Text(
                    text = when (uiState.dailyEditMode) {
                        DailyEditMode.ADD -> stringResource(R.string.button_daily_add_complete)
                        DailyEditMode.EDIT -> stringResource(R.string.button_daily_edit_complete)
                    }
                )
            }
        },
    ) { scaffoldPaddingValues ->
        Column(
            modifier = Modifier
                .padding(scaffoldPaddingValues)
                .background(
                    color = WH,
                    shape = RoundedCornerShape10
                )
                .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
        ) {
            Text(
                text = stringResource(R.string.text_per_daily),
                color = BL,
                fontSize = SP18,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = DP16, bottom = DP8, start = DP8)
            )
            DefaultTextField(
                value = uiState.title,
                onValueChange = onTitleChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                hint = { Text(stringResource(R.string.hint_daily_title)) }
            )
            Text(
                text = stringResource(R.string.select_week_days),
                color = BL,
                fontSize = SP18,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = DP32, bottom = DP8)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DP40)
            ) {
                DayOfWeek.entries.forEach { day ->
                    FilterChip(
                        isSelected = uiState.selectedDays.contains(day),
                        modifier = Modifier.noRippleClickable { onDayOfWeekChange(day) }
                    ) {
                        Text(day.getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    }
                }
            }
            Text(
                text = stringResource(R.string.select_time),
                color = BL,
                fontSize = SP18,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = DP32, bottom = DP8)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = DP52)
                    .background(color = WH, shape = RoundedCornerShape10)
                    .border(
                        width = dimensionResource(R.dimen.size_stroke_1),
                        color = G2,
                        shape = RoundedCornerShape10
                    )
                    .padding(horizontal = dimensionResource(R.dimen.padding_content_horizontal20))
                    .noRippleClickable { isTimePickerDialogShow = true }
            ) {
                Image(
                    painter = painterResource(ic_time_text_field),
                    contentDescription = stringResource(R.string.image_selected_time_description)
                )
                Text(
                    text = uiState.time.getTimeStamp(context),
                    color = BL,
                    fontSize = SP20,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = DP20)
                )
            }
        }
    }
}

private const val TWELVE = 12
private const val Zero = 0
private const val EMPTY = ""

private fun LocalTime.getTimeStamp(context: Context): String = with(context) {
    val hour: String = hour.takeIf { hour <= TWELVE }
        ?.let { "${getString(R.string.am)} $hour" }
        ?: "${getString(R.string.pm)} ${hour - TWELVE}"

    val minute = minute.takeIf { it != Zero }
        ?.let { it.toString() + getString(R.string.minute) }

    return hour + getString(R.string.hour) + (minute?.let { " $it" } ?: EMPTY)
}

@Preview
@Composable
private fun DailyEditScreenPreview() {
    HarmonyTheme {
        DailyEditScreen(
            onDismissRequest = {},
            onCompleteRequest = {},
            onTitleChange = {},
            onDayOfWeekChange = {},
            onTimeChange = { hour, minute -> },
            uiState = DailyEditUiState(
                title = "아침 식사 먹기",
                selectedDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            )
        )
    }
}