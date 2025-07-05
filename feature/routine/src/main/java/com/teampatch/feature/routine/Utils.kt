package com.teampatch.feature.routine

import android.content.Context
import java.time.DayOfWeek
import java.time.LocalTime

internal fun Context.periodStringFormat(
    daysOfWeek: Set<DayOfWeek>,
    time: LocalTime
): String {
    val daysOfWeek: String = if (daysOfWeek.size == 7) getString(R.string.everyday)
    else daysOfWeek.joinToString(", ") { it.toStringFormat(this) }

    val hour: String = if (time.hour <= 12) {
        getString(R.string.am)
    } else {
        getString(R.string.pm)
    }
        .let { it + " " + time.hour + getString(R.string.hour) }

    return getString(R.string.text_management_date_format, daysOfWeek, hour)
}

internal fun LocalTime.toStringFormat(context: Context): String = with(context) {
    val minute = minute.takeIf { it > 0 }

    when (hour) {
        12 -> minute?.let { getString(R.string.routine_text_time_format_pm, 12, it) }
            ?: getString(R.string.routine_text_hour_format_pm, 12)

        24 -> minute?.let { getString(R.string.routine_text_time_format_am, 12, it) }
            ?: getString(R.string.routine_text_hour_format_am, 12)

        in 1..11 -> minute?.let { getString(R.string.routine_text_time_format_pm, hour, minute) }
            ?: getString(R.string.routine_text_hour_format_pm, hour)

        else -> minute?.let { getString(R.string.routine_text_time_format_pm, hour - 12, minute) }
            ?: getString(R.string.routine_text_hour_format_pm, hour - 12)
    }
}

internal fun DayOfWeek.toStringFormat(context: Context): String = when (this) {
    DayOfWeek.MONDAY -> context.getString(R.string.monday)
    DayOfWeek.TUESDAY -> context.getString(R.string.tuesday)
    DayOfWeek.WEDNESDAY -> context.getString(R.string.wednesday)
    DayOfWeek.THURSDAY -> context.getString(R.string.thursday)
    DayOfWeek.FRIDAY -> context.getString(R.string.friday)
    DayOfWeek.SATURDAY -> context.getString(R.string.saturday)
    DayOfWeek.SUNDAY -> context.getString(R.string.sunday)
}