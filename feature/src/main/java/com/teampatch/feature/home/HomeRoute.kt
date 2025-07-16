package com.teampatch.feature.home

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.designsystem.model.CheckableData
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.core.domain.model.user.Role
import com.teampatch.feature.R
import com.teampatch.feature.home.model.HomeEvent
import com.teampatch.feature.home.model.HomeUiState

@Composable
internal fun HomeRoute(
    onUserPageRequest: () -> Unit,
    onMemoryCardCreationPageRequest: () -> Unit,
    onDailyRoutineRegisterPageRequest: () -> Unit,
    onDailyRoutineClick: (dailyRoutineId: String) -> Unit,
    onMemoryCardClick: (memoryCardId: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val dailyRoutine: List<CheckableData<DailyRoutine>> by viewModel.dailyRoutine.collectAsStateWithLifecycle()
    val uiState: HomeUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.role) {
        Role.VIP -> {
            VipHomeScreen(
                onUserPageRequest = onUserPageRequest,
                onDailyRoutineRegisterPageRequest = onDailyRoutineRegisterPageRequest,
                onDailyRoutineClick = onDailyRoutineClick,
                onMemoryCardClick = onMemoryCardClick,
                onDailyRoutineCheckChanged = viewModel::changeDailyRoutine,
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
                onDailyRoutineCheckChanged = viewModel::changeDailyRoutine,
                uiState = uiState,
                dailyRoutine = dailyRoutine
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is HomeEvent.DailyRoutineUpdateError -> Toast.makeText(
                    context,
                    R.string.home_toast_daily_routine_change_error,
                    Toast.LENGTH_SHORT
                ).show()

                is HomeEvent.MemoryCardAdditionError -> Toast.makeText(
                    context,
                    R.string.home_toast_memory_card_add_error,
                    Toast.LENGTH_SHORT
                ).show()

                HomeEvent.InitDataLoadError -> Toast.makeText(
                    context,
                    R.string.home_toast_init_data_load_error,
                    Toast.LENGTH_SHORT
                ).show()

                HomeEvent.MemoryCardReceiveError -> Toast.makeText(
                    context,
                    R.string.home_toast_memory_card_receive_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}