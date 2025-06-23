package com.teampatch.feature.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.teampatch.core.domain.model.Role
import com.teampatch.feature.home.model.HomeErrorHandler

@Composable
internal fun HomeRoute(
    onUserPageRequest: () -> Unit,
    onDailyRoutineRegisterPageRequest: () -> Unit,
    onDailyRoutineClick: (dailyRoutineId: String) -> Unit,
    onMemoryCardClick: (memoryCardId: String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val dailyRoutine = homeViewModel.dailyRoutine.collectAsLazyPagingItems()
    val memoryCardUiState by homeViewModel.memoryCardUiState.collectAsStateWithLifecycle()
    val errorHandler by homeViewModel.errorHandler.collectAsStateWithLifecycle(null)
    val user by homeViewModel.user.collectAsStateWithLifecycle()

    when (user?.role) {
        null -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        Role.VIP -> {
            VipHomeScreen(
                onUserPageRequest = onUserPageRequest,
                onDailyRoutineRegisterPageRequest = onDailyRoutineRegisterPageRequest,
                onDailyRoutineClick = onDailyRoutineClick,
                onMemoryCardClick = onMemoryCardClick,
                onDailyRoutineCheckChanged = homeViewModel::changeDailyRoutine,
                memoryCardUiState = memoryCardUiState,
                dailyRoutine = dailyRoutine
            )
        }

        Role.MEMBER -> {
            MemberHomeScreen(
                onUserPageRequest = onUserPageRequest,
                onDailyRoutineClick = onDailyRoutineClick,
                onMemoryCardClick = onMemoryCardClick,
                onDailyRoutineCheckChanged = homeViewModel::changeDailyRoutine,
                uploadMemoryCardRequest = homeViewModel::addMemoryCard,
                memoryCardUiState = memoryCardUiState,
                dailyRoutine = dailyRoutine
            )
        }
    }

    LaunchedEffect(errorHandler) {
        when (errorHandler) {
            null -> {}
            is HomeErrorHandler.ChangeDailyRoutineError -> {
                Toast.makeText(context, "일과 변경중에 에러가 발생되었습니다.", Toast.LENGTH_SHORT).show()
            }

            is HomeErrorHandler.MemoryCardAdditionError -> {
                Toast.makeText(context, "추억카드 등록중에 에러가 발생되었습니다.", Toast.LENGTH_SHORT).show()
            }

            is HomeErrorHandler.UserInfoLoadError -> {
                Toast.makeText(context, "유저 정보를 불러오는 도중에 에러가 발생되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}