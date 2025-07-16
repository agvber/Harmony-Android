package com.teampatch.feature.routine.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.teampatch.core.designsystem.theme.HarmonyTheme
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal class RoutineAlarmActivity : ComponentActivity() {

    private lateinit var title: String
    private lateinit var time: LocalTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loadIntentData()
        initView()
        setScreenPreference()
    }

    private fun loadIntentData() {
        title = intent.getStringExtra(TITLE_PARAM) ?: ""
        time = intent.getStringExtra(TIME_PARAM)
            ?.let { runCatching { LocalTime.parse(it, timeFormat) } }
            ?.getOrNull()
            ?: LocalTime.now()
        Log.d(TAG, "title: $title, time: $time")
    }

    private fun initView() = setContent {
        HarmonyTheme {
            RoutineAlarmScreen(
                onLaterTaskRequest = { finish() },
                onHistoryPageRequest = {},
                time = time,
                title = title
            )
        }
    }

    private fun setScreenPreference() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    }

    companion object {

        const val TAG: String = "RoutineAlarmActivityActivity"

        private const val TITLE_PARAM: String = "title"
        private const val TIME_PARAM: String = "time"
        private const val DAY_OF_WEEK_PARAM: String = "dayOfWeek"

        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        fun getPendingIntent(
            context: Context,
            routineId: Int,
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
            }
            return PendingIntent.getActivity(
                context,
                routineId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }
    }
}