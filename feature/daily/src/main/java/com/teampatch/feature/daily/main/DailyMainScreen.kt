package com.teampatch.feature.daily.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.teampatch.core.common.getOrNull
import com.teampatch.core.designsystem.R.drawable.ic_edit
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DailyRoutineCard
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.designsystem.preview.TodoPreviewParameterProvider
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.model.Todo
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.main.model.DailyMainEvent
import com.teampatch.feature.daily.main.model.DailyMainUiState
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun DailyMainScreenWithViewModel(
    onEditPageRequest: () -> Unit,
    onDetailPageRequest: (dailyId: String) -> Unit,
    viewModel: DailyMainViewModel = hiltViewModel()
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current
    val todos: LazyPagingItems<CheckableData<Todo>> = viewModel.todos.collectAsLazyPagingItems()
    val uiState: DailyMainUiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isLoading) {
        DailyMainScreen(
            onEditPageRequest = onEditPageRequest,
            onDetailPageRequest = onDetailPageRequest,
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = todos,
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
                }
            }
    }
}

@Composable
private fun DailyMainScreen(
    onEditPageRequest: () -> Unit,
    onDetailPageRequest: (dailyId: String) -> Unit,
    onDailyRoutineCheckChanged: (todoId: String, checked: Boolean) -> Unit,
    dailyRoutine: LazyPagingItems<CheckableData<Todo>>,
    uiState: DailyMainUiState,
) {
    val context: Context = LocalContext.current
    val progressIntFormat: Int by remember(uiState.progress) {
        derivedStateOf {
            (uiState.progress * 100).toInt()
        }
    }
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
                    .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
            )
        }
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
                        progress = { uiState.progress },
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
            items(dailyRoutine.itemCount) { index ->
                val currentItem = dailyRoutine.getOrNull(index)?.data
                    .also {
                        if (it == null)
                            Log.d(SCREEN_TAG, "DailyMainScreen: item is null. index: $index")
                    }
                    ?: return@items
                DailyRoutineCard(
                    onCheckedChange = {
                        dailyRoutine.itemSnapshotList.items.getOrNull(index)?.checked?.value = it
                        onDailyRoutineCheckChanged(currentItem.id, it)
                    },
                    title = {
                        Text(
                            text = context.convertHourStringFormat(currentItem.dateTime.hour),
                            maxLines = 1
                        )
                    },
                    content = { Text(text = currentItem.title, maxLines = 2) },
                    checked = currentItem.isFinished,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
                        .background(WH)
                        .noRippleClickable { onDetailPageRequest(currentItem.id) }
                )
            }
            if (dailyRoutine.itemCount != 0) {
                item {
                    Box(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

private const val SCREEN_TAG = "DailyMainScreen"

internal fun Context.convertHourStringFormat(hour: Int) = when (hour) {
    0 -> getString(R.string.text_date_am, 12)
    in 1..12 -> getString(R.string.text_date_am, hour)
    else -> getString(R.string.text_date_pm, hour - 12)
}

@Preview
@Composable
private fun DailyMainScreenPreview() {
    HarmonyTheme {
        val todos = TodoPreviewParameterProvider().values.first()
            .map { CheckableData(it, mutableStateOf(it.isFinished)) }
            .let { flowOf(PagingData.from(it)) }
            .collectAsLazyPagingItems()

        DailyMainScreen(
            onEditPageRequest = {},
            onDetailPageRequest = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = todos,
            uiState = DailyMainUiState(progress = .5f)
        )
    }
}