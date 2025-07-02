package com.teampatch.feature.daily.main

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_edit
import com.teampatch.core.designsystem.R.drawable.ic_fab_plus
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DailyRoutineCard
import com.teampatch.core.designsystem.component.ItemFloatingButton
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeDailyRoutine
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.main.model.DailyMainEvent
import com.teampatch.feature.daily.main.model.DailyMainUiState

@Composable
internal fun DailyMainScreenWithViewModel(
    onCreationPageRequest: () -> Unit,
    onEditPageRequest: () -> Unit,
    onDetailPageRequest: (dailyId: String) -> Unit,
    viewModel: DailyMainViewModel = hiltViewModel()
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current
    val uiState: DailyMainUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dailyRoutine: List<CheckableData<DailyRoutine>> by viewModel.dailyRoutine.collectAsStateWithLifecycle()

    if (!uiState.isLoading) {
        DailyMainScreen(
            onCreationPageRequest = onCreationPageRequest,
            onEditPageRequest = onEditPageRequest,
            onDetailPageRequest = onDetailPageRequest,
            onDailyRoutineCheckChanged = viewModel::toggleRoutineFinished,
            dailyRoutine = dailyRoutine,
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is DailyMainEvent.LoadError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_main_load_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is DailyMainEvent.RoutineStatusChangedError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_main_status_changed_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}

@Composable
private fun DailyMainScreen(
    onCreationPageRequest: () -> Unit,
    onEditPageRequest: () -> Unit,
    onDetailPageRequest: (dailyId: String) -> Unit,
    onDailyRoutineCheckChanged: (todoId: String, checked: Boolean) -> Unit,
    dailyRoutine: List<CheckableData<DailyRoutine>>,
    uiState: DailyMainUiState,
) {
    val context: Context = LocalContext.current
    val progressIntFormat: Int by remember(uiState.progress) {
        derivedStateOf {
            (uiState.progress * 100).toInt()
        }
    }
    val animatedFloatProgress by animateFloatAsState(uiState.progress)
    Scaffold(
        topBar = {
            AppBar(
                navigation = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BL)) {
                                val now = with(uiState.now) {
                                    stringResource(
                                        R.string.date_format,
                                        year,
                                        monthValue,
                                        dayOfMonth
                                    )
                                }
                                append(now)
                            }
                            withStyle(style = SpanStyle(color = MainGreen)) {
                                append(stringResource(R.string.appbar_main_title))
                            }
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    Image(
                        painter = painterResource(ic_edit),
                        contentDescription = stringResource(R.string.image_edit_content_description),
                        modifier = Modifier.noRippleClickable(onClick = onEditPageRequest)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_root_20)),
            )
        },
        floatingActionButton = {
            ItemFloatingButton(
                modifier = Modifier.noRippleClickable(onClick = onCreationPageRequest)
            ) {
                Image(
                    painter = painterResource(ic_fab_plus),
                    contentDescription = stringResource(R.string.image_add_description)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { scaffoldPaddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPaddingValues)
                .background(G1)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .background(WH)
                        .padding(
                            vertical = 16.dp,
                            horizontal = dimensionResource(R.dimen.padding_root_20)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.text_main_title),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = G5,
                        lineHeight = 30.sp
                    )
                    Text(
                        text = stringResource(
                            R.string.text_main_progress,
                            progressIntFormat
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MainGreen,
                        lineHeight = 30.sp
                    )
                    LinearProgressIndicator(
                        progress = { animatedFloatProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.size_radius_999))),
                        color = MainGreen,
                        trackColor = G1
                    )
                }
            }
            items(dailyRoutine) { currentItem ->
                DailyRoutineCard(
                    onCheckedChange = {
                        val index = dailyRoutine.indexOf(currentItem)
                        dailyRoutine.getOrNull(index)?.checked?.value = it
                        onDailyRoutineCheckChanged(currentItem.data.routineId, it)
                    },
                    title = {
                        Text(
                            text = context.convertHourStringFormat(currentItem.data.time.hour),
                            maxLines = 1
                        )
                    },
                    content = { Text(text = currentItem.data.name, maxLines = 2) },
                    checked = currentItem.checked.value,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
                        .background(WH)
                        .noRippleClickable { onDetailPageRequest(currentItem.data.routineId) }
                )
            }
            if (dailyRoutine.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

internal fun Context.convertHourStringFormat(hour: Int) = when (hour) {
    0 -> getString(R.string.text_date_am, 12)
    in 1..12 -> getString(R.string.text_date_am, hour)
    else -> getString(R.string.text_date_pm, hour - 12)
}

@Preview
@Composable
private fun DailyMainScreenPreview() {
    HarmonyTheme {
        DailyMainScreen(
            onCreationPageRequest = {},
            onEditPageRequest = {},
            onDetailPageRequest = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = FakeDailyRoutine().get()
                .map { CheckableData(it, mutableStateOf(false)) },
            uiState = DailyMainUiState(progress = .5f)
        )
    }
}