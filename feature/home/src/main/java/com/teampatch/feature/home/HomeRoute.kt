package com.teampatch.feature.home

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.model.user.Role
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.feature.home.model.HomeEvent
import com.teampatch.feature.home.model.HomeUiState

@Composable
internal fun HomeRoute(
    onUserPageRequest: () -> Unit,
    onMemoryCardCreationPageRequest: () -> Unit,
    onDailyRoutineRegisterPageRequest: () -> Unit,
    onDailyRoutineClick: (dailyRoutineId: String) -> Unit,
    onMemoryCardClick: (memoryCardId: String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val dailyRoutine: List<CheckableData<DailyRoutine>> by homeViewModel.dailyRoutine.collectAsStateWithLifecycle()
    val uiState: HomeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val errorHandler: HomeEvent? by homeViewModel.event.collectAsStateWithLifecycle(
        null
    )

    when (uiState.role) {
        Role.VIP -> {
            VipHomeScreen(
                onUserPageRequest = onUserPageRequest,
                onDailyRoutineRegisterPageRequest = onDailyRoutineRegisterPageRequest,
                onDailyRoutineClick = onDailyRoutineClick,
                onMemoryCardClick = onMemoryCardClick,
                onDailyRoutineCheckChanged = homeViewModel::changeDailyRoutine,
                dailyRoutine = dailyRoutine,
                uiState = uiState
            )
        }

        Role.MEMBER -> {
            MemberHomeScreen(
                onUserPageRequest = onUserPageRequest,
                onMemoryCardCreationPageRequest = onMemoryCardCreationPageRequest,
                onDailyRoutineClick = onDailyRoutineClick,
                onMemoryCardClick = onMemoryCardClick,
                onDailyRoutineCheckChanged = homeViewModel::changeDailyRoutine,
                uiState = uiState,
                dailyRoutine = dailyRoutine
            )
        }
    }

    LaunchedEffect(errorHandler) {
        when (errorHandler) {
            null -> {}
            is HomeEvent.ChangeDailyRoutineError -> {
                Toast.makeText(context, "일과 변경중에 에러가 발생되었습니다.", Toast.LENGTH_SHORT).show()
            }

            is HomeEvent.MemoryCardAdditionError -> {
                Toast.makeText(context, "추억카드 등록중에 에러가 발생되었습니다.", Toast.LENGTH_SHORT).show()
            }

            is HomeEvent.UserInfoLoadError -> {
                Toast.makeText(context, "유저 정보를 불러오는 도중에 에러가 발생되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}