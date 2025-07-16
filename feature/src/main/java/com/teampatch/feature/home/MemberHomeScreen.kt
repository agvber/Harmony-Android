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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.teampatch.core.R.drawable.ic_my_appbar
import com.teampatch.core.R.drawable.img_test_memory_card
import com.teampatch.core.designsystem.component.AdditionMemoryCard
import com.teampatch.core.designsystem.component.CollapseMemoryCard
import com.teampatch.core.designsystem.component.DailyRoutineCard
import com.teampatch.core.designsystem.component.ExpandMemoryCard
import com.teampatch.core.designsystem.component.HomeAppBar
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP28
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeDailyRoutine
import com.teampatch.core.domain.fake.FakeMemoryCard
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.feature.R
import com.teampatch.feature.home.model.HomeUiState

@Composable
internal fun MemberHomeScreen(
    onUserPageRequest: () -> Unit,
    onMemoryCardCreationPageRequest: () -> Unit,
    onDailyRoutineClick: (id: String) -> Unit,
    onMemoryCardClick: (id: String) -> Unit,
    onDailyRoutineCheckChanged: (id: String, checked: Boolean) -> Unit,
    uiState: HomeUiState,
    dailyRoutine: List<CheckableData<DailyRoutine>>,
) {
    val context: Context = LocalContext.current
    var memoryCardExpanded by rememberSaveable { mutableStateOf(false) }
    var isMemoryCardCreationDialogShow by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HomeAppBar {
            Image(
                painter = painterResource(ic_my_appbar),
                contentDescription = "my",
                modifier = Modifier
                    .padding(end = DP20)
                    .noRippleClickable(onClick = onUserPageRequest)
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(DP12),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(G1)
                        .padding(top = DP28, bottom = DP24)
                        .noRippleClickable {
                            if (!memoryCardExpanded) {
                                memoryCardExpanded = true
                                return@noRippleClickable
                            }
                            uiState.memoryCard?.let { onMemoryCardClick(it.id) }
                        }
                ) {
                    uiState.memoryCard?.let { memoryCard ->
                        if (memoryCardExpanded) {
                            ExpandMemoryCard(
                                title = memoryCard.text,
                                description = with(memoryCard.dateTime) {
                                    stringResource(
                                        R.string.home_text_memory_card_date_format,
                                        year, monthValue, dayOfMonth
                                    )
                                },
                                painter = painterResource(img_test_memory_card)
                            )
                            return@let
                        }
                        CollapseMemoryCard(
                            title = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = BL)) {
                                    append("${stringResource(R.string.member_home_text_memory_card_title1)} ")
                                }
                                withStyle(style = SpanStyle(color = MainGreen)) {
                                    append(stringResource(R.string.member_home_text_memory_card_title2))
                                }
                                withStyle(style = SpanStyle(color = BL)) {
                                    append(stringResource(R.string.member_home_text_memory_card_title3))
                                }
                            },
                            text = stringResource(R.string.member_home_text_memory_card_content),
                            writer = memoryCard.let { "${it.writerTitle} ${it.writerName}" }
                        )
                    } ?: AdditionMemoryCard(
                        onClick = { isMemoryCardCreationDialogShow = true },
                        title = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BL)) {
                                append("${stringResource(R.string.member_home_text_empty_memory_card_title1)} ")
                            }
                            withStyle(style = SpanStyle(color = MainGreen)) {
                                append(stringResource(R.string.member_home_text_empty_memory_card_title2))
                            }
                            withStyle(style = SpanStyle(color = BL)) {
                                append(stringResource(R.string.member_home_text_empty_memory_card_title3))
                            }
                        },
                        text = stringResource(R.string.member_home_text_memory_card_send)
                    )
                }

            }

            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MainGreen)) {
                            with(uiState.now) {
                                append(
                                    stringResource(
                                        R.string.home_text_title_date_format,
                                        monthValue,
                                        dayOfMonth
                                    )
                                )
                            }
                        }
                        withStyle(SpanStyle(BL)) {
                            append(stringResource(R.string.hone_text_daily_routine))
                        }
                    },
                    fontWeight = FontWeight.W500,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(start = DP24, top = DP8, bottom = DP12)
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
                        .padding(horizontal = DP24)
                        .noRippleClickable { onDailyRoutineClick(dailyRoutine.data.routineId) }
                )
            }

            if (dailyRoutine.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.height(DP20))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MemberHomeScreenPreview() {
    HarmonyTheme {
        MemberHomeScreen(
            onUserPageRequest = {},
            onMemoryCardCreationPageRequest = {},
            onDailyRoutineClick = {},
            onMemoryCardClick = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = FakeDailyRoutine().get().map {
                CheckableData(it, mutableStateOf(false))
            },
            uiState = HomeUiState(memoryCard = FakeMemoryCard().get()[0])
        )
    }
}

@Preview
@Composable
private fun MemberHomeScreenEmptyPreview() {
    HarmonyTheme {
        MemberHomeScreen(
            onUserPageRequest = {},
            onMemoryCardCreationPageRequest = {},
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