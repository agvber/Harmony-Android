package com.teampatch.harmony.routine.entities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.teampatch.core.domain.entities.RoutineAlarmManager
import com.teampatch.harmony.routine.RoutineAlarmActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

class RoutineAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : RoutineAlarmManager {

    private val alarmManager: AlarmManager by lazy {
        appContext.getSystemService<AlarmManager>(AlarmManager::class.java)
    }

    override fun setRoutineAlarm(
        routineId: Int,
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val now: LocalDateTime = LocalDateTime.now()
        val triggerCalendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, periodTime.hour)
            set(Calendar.MINUTE, periodTime.minute)
            set(Calendar.SECOND, 0)
        }

        daysOfWeekPeriod.forEach { dayOfWeek ->
            val pendingIntent: PendingIntent = RoutineAlarmActivity.getPendingIntent(
                context = appContext,
                routineId = routineId.toString(),
                title = name,
                time = periodTime,
                dayOfWeek = dayOfWeek
            )
            val repeatWeekCount: Int = (dayOfWeek.value - now.dayOfWeek.value + 1)
                .let { if (it <= 0) it + 7 else it }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * repeatWeekCount,
                pendingIntent
            )
        }
    }

    override fun cancelRoutineAlarm(
        routineId: Int,
        daysOfWeekPeriod: Set<DayOfWeek>,
    ) {
        daysOfWeekPeriod.forEach { dayOfWeek ->
            val pendingIntent: PendingIntent = RoutineAlarmActivity.getPendingIntent(
                context = appContext,
                routineId = routineId,
                dayOfWeek = dayOfWeek
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}