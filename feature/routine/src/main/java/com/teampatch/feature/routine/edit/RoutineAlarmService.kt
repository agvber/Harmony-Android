package com.teampatch.feature.routine.edit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.teampatch.feature.routine.alarm.RoutineAlarmActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

internal class RoutineAlarmService @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private val alarmManager: AlarmManager by lazy {
        appContext.getSystemService<AlarmManager>(AlarmManager::class.java)
    }

    fun setRoutineAlarm(
        routineId: Int,
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, periodTime.hour)
            set(Calendar.MINUTE, periodTime.minute)
        }
        val dayOfWeekPeriod: DayOfWeek = when (val key = calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            else -> DayOfWeek.of(key - 1)
        }

        daysOfWeekPeriod.forEach { dayOfWeek ->
            val pendingIntent: PendingIntent = RoutineAlarmActivity.getPendingIntent(
                context = appContext,
                routineId = routineId,
                title = name,
                time = periodTime,
                dayOfWeek = dayOfWeek
            )
            val repeatWeekCount: Int = (dayOfWeek.value - dayOfWeekPeriod.value + 1)
                .let { if (it <= 0) it + 7 else it }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * repeatWeekCount,
                pendingIntent
            )
        }
    }

    fun cancelRoutineAlarm(
        routineId: Int,
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        daysOfWeekPeriod.forEach { dayOfWeek ->
            val pendingIntent: PendingIntent = RoutineAlarmActivity.getPendingIntent(
                context = appContext,
                routineId = routineId,
                title = name,
                time = periodTime,
                dayOfWeek = dayOfWeek
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}