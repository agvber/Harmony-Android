package com.teampatch.feature.daily.management

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_more_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.SubRed
import com.teampatch.core.domain.fake.FakeDailyManage
import com.teampatch.core.domain.model.DailyManage
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.management.model.DailyManagementEvent
import com.teampatch.feature.daily.management.model.DailyManagementUiState

@Composable
internal fun DailyManagementScreenWithViewModel(
    onBackRequest: () -> Unit,
    onEditPageRequest: (dailyId: String) -> Unit,
    viewModel: DailyManagementViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState: DailyManagementUiState by viewModel.dailyManagementUiState

    if (!uiState.isLoading) {
        DailyManagementScreen(
            onBackRequest = onBackRequest,
            onEditDailyRequest = { uiState.dailyManage?.id?.let { onEditPageRequest(it) } },
            onDeleteDailyRequest = { },
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.event.collect {
                when (it) {
                    is DailyManagementEvent.LoadError ->
                        Toast.makeText(context, "데이터를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
internal fun DailyManagementScreen(
    onBackRequest: () -> Unit,
    onEditDailyRequest: (DailyManage) -> Unit,
    onDeleteDailyRequest: (DailyManage) -> Unit,
    uiState: DailyManagementUiState,
) {
    val daily = uiState.dailyManage
    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = {
                    Text(stringResource(R.string.text_management_appbar))
                }
            )
        }
    ) { scaffoldPaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPaddingValues)
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (daily == null) {
                CircularProgressIndicator() // 로딩 상태 처리
            } else {
                DailyItem(
                    dailyItem = daily,
                    onEditDailyRequest = { onEditDailyRequest(daily) },
                    onDeleteDailyRequest = { onDeleteDailyRequest(daily) }
                )
            }
        }
    }
}

@Composable
fun DailyItem(
    dailyItem: DailyManage,
    onEditDailyRequest: () -> Unit,
    onDeleteDailyRequest: () -> Unit,
) {
    var isDropDownMenuShow by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(G1, RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "#${dailyItem.number}",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = G4
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { isDropDownMenuShow = true }
                ) {
                    Icon(
                        painter = painterResource(ic_more_question),
                        contentDescription = "more",
                        tint = G5,
                        modifier = Modifier
                            .size(width = 4.dp, height = 16.dp)
                            .align(Alignment.CenterEnd)
                    )
                    DropdownMenu(
                        expanded = isDropDownMenuShow,
                        onDismissRequest = { isDropDownMenuShow = false },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = stringResource(R.string.dropdown_edit_daily),
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 20.sp,
                                        color = BL
                                    )
                                }
                            },
                            onClick = {
                                onEditDailyRequest()
                                isDropDownMenuShow = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = stringResource(R.string.dropdown_delete_daily),
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 20.sp,
                                        color = SubRed
                                    )
                                }
                            },
                            onClick = {
                                onDeleteDailyRequest()
                                isDropDownMenuShow = false
                            }
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Text(
                    text = dailyItem.title,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = BL,
                    modifier = Modifier.widthIn(max = 240.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DailyManagementScreenPreview() {
    HarmonyTheme {
        DailyManagementScreen(
            onBackRequest = {},
            onEditDailyRequest = {},
            onDeleteDailyRequest = {},
            uiState = DailyManagementUiState(dailyManage = FakeDailyManage().get())
        )
    }
}