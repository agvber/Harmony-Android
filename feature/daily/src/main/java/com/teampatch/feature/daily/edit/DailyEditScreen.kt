package com.teampatch.feature.daily.edit

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teampatch.core.designsystem.R.drawable.ic_close_memory_card
import com.teampatch.core.designsystem.R.drawable.ic_date_memory_card
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeDailyManage
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.edit.model.DailyEditEvent
import com.teampatch.feature.daily.edit.model.DailyEditUiState
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@Composable
internal fun DailyEditScreenWithViewModel(
    onDismissRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
    viewModel: DailyEditViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.dailyEditUiState
    if (!uiState.isLoading) {
        DailyEditScreen(
            onDismissRequest = onDismissRequest,
            onCompleteRequest = {
                onCompleteRequest(it)
            },
            uiState = uiState,
            selectedDays = uiState.selectedDays,
            onDaySelected = { viewModel.toggleSelectedDay(it) },
            onChangeDaily = { viewModel.changeDailyContent(it) }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is DailyEditEvent.AddDailyError -> {
                    Toast.makeText(context, "서버로 부터 데이터 전송 오류", Toast.LENGTH_LONG).show()
                }

                is DailyEditEvent.LoadError -> {
                    Toast.makeText(context, "데이터를 불러오지 못하였습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Composable
internal fun DailyEditScreen(
    onDismissRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
    uiState: DailyEditUiState,
    selectedDays: Set<DayOfWeek>,
    onDaySelected: (DayOfWeek) -> Unit,
    onChangeDaily: (String) -> Unit,
) {
    val context = LocalContext.current
    val daily = uiState.dailyExpand.content
    var time: LocalTime? by rememberSaveable { mutableStateOf(null) }
    val calendar = remember { Calendar.getInstance() }
    val hour = remember { calendar.get(Calendar.HOUR_OF_DAY) }
    val minute = remember { calendar.get(Calendar.MINUTE) }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                time = LocalTime.of(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true
        )
    }
    val daysOfWeek = remember { DayOfWeek.entries }

    Scaffold(
        topBar = {
            AppBar(
                title = { Text(stringResource(R.string.text_daily_appbar)) },
                actions = {
                    Image(
                        painter = painterResource(ic_close_memory_card),
                        contentDescription = "close",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .noRippleClickable(onClick = onDismissRequest)
                    )
                }
            )
        },
        bottomBar = {
            DefaultButton(
                onClick = { onCompleteRequest(daily) },
                enabled = daily.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.btn_complete_daily))
            }
        }
    ) { scaffoldPaddingValues ->
        Column(
            modifier = Modifier
                .padding(scaffoldPaddingValues)
                .height(IntrinsicSize.Max)
                .background(
                    color = WH,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(top = 24.dp, bottom = 14.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_per_daily),
                fontFamily = PretendardFontFamily,
                color = BL,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                DefaultTextField(
                    value = uiState.dailyExpand.content,
                    onValueChange = {
                        if (it.length <= 200) {
                            onChangeDaily(it) // ViewModel의 함수 호출
                        }
                    },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                    modifier = Modifier.height(IntrinsicSize.Max)
                )
            }

            Text(
                text = stringResource(R.string.select_week_days),
                fontFamily = PretendardFontFamily,
                color = BL,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                daysOfWeek.forEach { day ->
                    FilterChip(
                        selected = selectedDays.contains(day),
                        onClick = { onDaySelected(day) },
                        label = { Text(day.getDisplayName(TextStyle.SHORT, Locale.KOREAN)) },
                        modifier = Modifier.padding(horizontal = 2.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Green
                        )
                    )
                }
            }

            Text(
                text = stringResource(R.string.select_time),
                fontFamily = PretendardFontFamily,
                color = BL,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(color = WH, shape = RoundedCornerShape(10.dp))
                    .border(width = 1.dp, color = G2, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp)
                    .noRippleClickable { timePickerDialog.show() }
            ) {
                Image(
                    painter = painterResource(ic_date_memory_card),
                    contentDescription = "time"
                )
                Text(
                    text = time?.let { String.format("%02d:%02d", it.hour, it.minute) }
                        ?: stringResource(R.string.select_time),
                    color = BL,
                    fontSize = 20.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DailyEditScreenPreview() {
    HarmonyTheme {
        var selectedDays by remember {
            mutableStateOf(
                setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY
                )
            )
        }

        DailyEditScreen(
            onDismissRequest = {},
            onCompleteRequest = {},
            uiState = DailyEditUiState(
                dailyExpand = FakeDailyManage().get(),
                isLoading = false,
                selectedDays = selectedDays
            ),
            selectedDays = selectedDays,
            onDaySelected = { day ->
                selectedDays = selectedDays.toMutableSet().apply {
                    if (contains(day)) remove(day) else add(day)
                }
            },
            onChangeDaily = {}
        )
    }
}