package com.teampatch.harmony.routine

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.teampatch.core.common.flowExceptionSafety
import com.teampatch.core.domain.usecase.routine.GetRoutinesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@AndroidEntryPoint
class RoutineAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getRoutinesUseCase: GetRoutinesUseCase

    private val coroutineScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob())
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Manifest.permission.RECEIVE_BOOT_COMPLETED) return

        with(goAsync()) {
            flowExceptionSafety { getRoutinesUseCase.invoke() }
                .catch { it.printStackTrace() }
                .launchIn(coroutineScope)
                .invokeOnCompletion { finish() }
        }
    }
}