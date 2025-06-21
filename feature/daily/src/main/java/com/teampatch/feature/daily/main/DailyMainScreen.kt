package com.teampatch.feature.daily.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.teampatch.core.designsystem.R.drawable.ic_edit
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DailyRoutineCard
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.designsystem.preview.TodoPreviewParameterProvider
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.model.Todo
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.main.model.DailyMainEvent
import com.teampatch.feature.daily.main.model.DailyMainUiState
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@Composable
internal fun DailyMainScreenWithViewModel(
    onEditPageRequest: () -> Unit,
) {
    val context = LocalContext.current
    val dailyMainViewModel: DailyMainViewModel = hiltViewModel()
    val uiState by dailyMainViewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isLoading) {
        DailyMainScreen(
            progress = 0f,
            onDailyRoutineClick = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = uiState.daily.collectAsLazyPagingItems(),
            dailyExpandPageRequest = onEditPageRequest,
            uiState = uiState
        )
    }
    LaunchedEffect(Unit) {
        dailyMainViewModel.event.collect { sideEffect ->
            when (sideEffect) {
                is DailyMainEvent.LoadError -> {
                    Toast.makeText(context, "데이터를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
internal fun DailyMainScreen(
    progress: Float, // 진행률 (0f부터 1f까지의 값)
    onDailyRoutineClick: (String) -> Unit, // id
    onDailyRoutineCheckChanged: (String, Boolean) -> Unit, // id, checked
    dailyRoutine: LazyPagingItems<CheckableData<Todo>>,
    dailyExpandPageRequest: () -> Unit,
    uiState: DailyMainUiState,
) {
    Scaffold(
        topBar = {
            AppBar(
                navigation = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BL)) {
                                append(stringArrayResource(id = R.array.text_title_appbar)[0])
                            }
                            withStyle(style = SpanStyle(color = MainGreen)) {
                                append(stringArrayResource(id = R.array.text_title_appbar)[1])
                            }
                        },
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    Image(
                        painter = painterResource(ic_edit),
                        contentDescription = "edit",
                        modifier = Modifier
                            .padding(end = 21.dp)
                            .clickable { dailyExpandPageRequest() }
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
        }
    ) { scaffoldPaddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPaddingValues)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 17.dp) // 패딩 조정
                ) {
                    Text(
                        text = "완료된 일과에 응원의 한 마디를 남겨요!",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = G5,
                        modifier = Modifier.fillMaxWidth() // 가로 너비 최대 설정
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // 간격 추가
                    Text(
                        text = "33% 완료",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MainGreen,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray) // 배경 색상
                    ) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFF4CAF50) // 프로그레스 바 색상
                        )
                    }
                }
            }
            items(dailyRoutine.itemCount) { index ->
                val lastDateTime =
                    if (index > 0) dailyRoutine.peek(index - 1)?.data?.dateTime else null
                val dateTime = dailyRoutine.peek(index)?.data?.dateTime
                val title = dailyRoutine[index]?.data?.title
                DailyRoutineCard(
                    onCheckedChange = {
                        val data = dailyRoutine[index]?.data ?: return@DailyRoutineCard
                        dailyRoutine.itemSnapshotList.items[index].checked.value = it
                        onDailyRoutineCheckChanged(data.id, it)
                    },
                    checked = dailyRoutine[index]?.checked?.value ?: false,
                    dateTime = dateTime?.stringHour() ?: "",
                    text = title ?: "",
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .noRippleClickable {
                            val data = dailyRoutine[index]?.data ?: return@noRippleClickable
                            onDailyRoutineClick(data.id)
                        }
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

internal fun LocalDateTime.stringHour(): String = when (hour) {
    0 -> "오전 12시"
    !in 0..12 -> "오후 ${hour - 12}시"
    else -> "오전 ${hour}시"
}

@Preview
@Composable
private fun DailyMainScreenPreview() {
    HarmonyTheme {
        DailyMainScreen(
            progress = 0f,
            onDailyRoutineClick = {},
            onDailyRoutineCheckChanged = { _, _ -> },
            dailyRoutine = flowOf(
                PagingData.from(
                    data = TodoPreviewParameterProvider().values.first()
                        .map { CheckableData(it, mutableStateOf(it.isFinished)) }
                )
            )
                .collectAsLazyPagingItems(),
            dailyExpandPageRequest = { },
            uiState = DailyMainUiState()
        )
    }
}