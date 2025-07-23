package com.teampatch.harmony.routine

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.feature.routine.alarm.RoutineAlarmRoute
import com.teampatch.feature.routine.alarm.addRoutineAlarmScreen
import com.teampatch.feature.routine.certification.addRoutineCertificationScreen
import com.teampatch.feature.routine.certification.navigateToRoutineCertificationScreen
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RoutineAlarmActivity : ComponentActivity() {

    private lateinit var routineId: String
    private lateinit var title: String
    private lateinit var time: LocalTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initIntentDataLoad()
        initView()
        setScreenPreference()
    }

    private fun initIntentDataLoad() = with(intent) {
        routineId = getStringExtra(ROUTINE_ID_PARAM) ?: ""
        title = getStringExtra(TITLE_PARAM) ?: ""
        time = getStringExtra(TIME_PARAM)
            ?.let { runCatching { LocalTime.parse(it, timeFormat) } }
            ?.getOrNull()
            ?: LocalTime.now()
        Log.d(TAG, "routineId: $routineId, title: $title, time: $time")
    }

    private fun initView() = setContent {
        HarmonyTheme {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = RoutineAlarmRoute(title, time)
            ) {
                addRoutineAlarmScreen(
                    onLaterTaskRequest = { finish() },
                    onHistoryPageRequest = {
                        navController.navigateToRoutineCertificationScreen(routineId)
                    }
                )
                addRoutineCertificationScreen(
                    onBackRequest = { finish() }
                )
            }
        }
    }

    private fun setScreenPreference() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    }

    companion object {

        const val TAG: String = "RoutineAlarmActivityActivity"

        private const val ROUTINE_ID_PARAM: String = "routine_id"
        private const val TITLE_PARAM: String = "title"
        private const val TIME_PARAM: String = "time"
        private const val DAY_OF_WEEK_PARAM: String = "dayOfWeek"

        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        fun getPendingIntent(
            context: Context,
            routineId: String,
            title: String,
            time: LocalTime,
            dayOfWeek: DayOfWeek
        ): PendingIntent {
            val intent = Intent(context, RoutineAlarmActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(TITLE_PARAM, title)
                putExtra(TIME_PARAM, time.format(timeFormat))
                putExtra(DAY_OF_WEEK_PARAM, dayOfWeek.value)
                putExtra(ROUTINE_ID_PARAM, routineId)
            }
            return PendingIntent.getActivity(
                context,
                "${routineId}_${dayOfWeek.name}".hashCode(),
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }

        fun getPendingIntent(
            context: Context,
            routineId: Int,
            dayOfWeek: DayOfWeek
        ): PendingIntent {
            val intent = Intent(context, RoutineAlarmActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            return PendingIntent.getActivity(
                context,
                "${routineId}_${dayOfWeek.name}".hashCode(),
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }
    }
}