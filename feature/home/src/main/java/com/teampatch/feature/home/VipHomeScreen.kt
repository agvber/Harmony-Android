package com.teampatch.feature.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.R.drawable.ic_my_appbar
import com.teampatch.core.designsystem.R.drawable.img_test_memory_card
import com.teampatch.core.designsystem.component.CollapseMemoryCard
import com.teampatch.core.designsystem.component.DailyRoutineCard
import com.teampatch.core.designsystem.component.EmptyLetterBox
import com.teampatch.core.designsystem.component.ExpandMemoryCard
import com.teampatch.core.designsystem.component.HomeAppBar
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeDailyRoutine
import com.teampatch.core.domain.fake.FakeMemoryCard
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.feature.home.model.HomeUiState
import com.teampatch.feature.home.model.MemoryCardState
import java.time.LocalTime

@Composable
internal fun VipHomeScreen(
    onUserPageRequest: () -> Unit,
    onDailyRoutineRegisterPageRequest: () -> Unit,
    onDailyRoutineClick: (id: String) -> Unit,
    onMemoryCardClick: (id: String) -> Unit,
    onDailyRoutineCheckChanged: (id: String, checked: Boolean) -> Unit,
    uiState: HomeUiState,
    dailyRoutine: List<CheckableData<DailyRoutine>>,
) {
    val context: Context = LocalContext.current
    val memoryCardState = uiState.memoryCardState
    var memoryCardExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HomeAppBar {
            Image(
                painter = painterResource(ic_my_appbar),
                contentDescription = "my",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .noRippleClickable(
                        onClick = onUserPageRequest
                    )
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(G1)
                        .padding(top = 28.dp, bottom = 24.dp)
                        .noRippleClickable {
                            if (memoryCardExpanded &&
                                memoryCardState is MemoryCardState.Success
                            ) {
                                onMemoryCardClick(memoryCardState.data.id)
                                return@noRippleClickable
                            }
                            memoryCardExpanded = true
                        }
                ) {
                    when (memoryCardState) {
                        is MemoryCardState.Success -> {
                            if (memoryCardExpanded) {
                                ExpandMemoryCard(
                                    memoryCardState.data.text,
                                    memoryCardState.data.dateTime.let {
                                        "${it.year}${stringResource(R.string.year)} " +
                                                "${it.monthValue}${stringResource(R.string.month)} " +
                                                "${it.dayOfMonth}${stringResource(R.string.day)}"
                                    },
                                    painter = painterResource(img_test_memory_card)
                                )
                                return@item
                            }

                            CollapseMemoryCard(
                                title = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = BL)) {
                                        append("${stringResource(R.string.text_vip_home_collapse_memory_card_title1)} ")
                                    }
                                    withStyle(style = SpanStyle(color = MainGreen)) {
                                        append(stringResource(R.string.text_vip_home_collapse_memory_card_title2))
                                    }
                                    withStyle(style = SpanStyle(color = BL)) {
                                        append(stringResource(R.string.text_vip_home_collapse_memory_card_title3))
                                    }
                                },
                                text = stringResource(R.string.text_vip_home_collapse_memory_card_content),
                                writer = memoryCardState.data.let { "${it.writerTitle} ${it.writerName}" }
                            )
                        }

                        else -> {
                            EmptyLetterBox()
                        }
                    }
                }
            }

            item {
                Text(
                    text = buildAnnotatedString {
                        with(uiState.now) {
                            withStyle(style = SpanStyle(color = MainGreen)) {
                                append(
                                    "${monthValue}${stringResource(R.string.month)} " +
                                            "${dayOfMonth}${stringResource(R.string.day)}"
                                )
                            }
                        }
                        withStyle(SpanStyle(BL)) {
                            append(stringResource(R.string.text_daily_routine_time_stamp))
                        }
                    },
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.W500,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 8.dp, bottom = 12.dp)
                )
            }

            items(dailyRoutine) { dailyRoutine ->
                DailyRoutineCard(
                    onCheckedChange = {
                        dailyRoutine.checked.value = it
                        onDailyRoutineCheckChanged(dailyRoutine.data.routineId, it)
                    },
                    checked = dailyRoutine.checked.value,
                    dateTime = dailyRoutine.data.time.toStringFormat(context),
                    text = dailyRoutine.data.name,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .noRippleClickable { onDailyRoutineClick(dailyRoutine.data.routineId) }
                )
            }

            if (dailyRoutine.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.height(20.dp))
                }
            }
        }

        if (dailyRoutine.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.text_daily_routine_empty_title),
                    color = G4,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .widthIn(min = 320.dp)
                        .heightIn(48.dp)
                        .background(MainGreen, RoundedCornerShape(999.dp))
                        .noRippleClickable(
                            onClick = onDailyRoutineRegisterPageRequest
                        )
                ) {
                    Text(
                        text = stringResource(R.string.btn_daily_routine_empty),
                        color = WH,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun VipHomeScreenPreview() {
    HarmonyTheme {
        VipHomeScreen(
            onUserPageRequest = {},
            onDailyRoutineRegisterPageRequest = {},
            onDailyRoutineClick = {},
            onMemoryCardClick = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = FakeDailyRoutine().get().map {
                CheckableData(it, mutableStateOf(false))
            },
            uiState = HomeUiState(
                memoryCardState = MemoryCardState.Success(
                    FakeMemoryCard().get()[0]
                ),
            )
        )
    }
}

@Preview
@Composable
private fun VipHomeScreenEmptyPreview() {
    HarmonyTheme {
        VipHomeScreen(
            onUserPageRequest = {},
            onDailyRoutineRegisterPageRequest = {},
            onDailyRoutineClick = {},
            onMemoryCardClick = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = FakeDailyRoutine().get().map {
                CheckableData(it, mutableStateOf(false))
            },
            uiState = HomeUiState()
        )
    }
}