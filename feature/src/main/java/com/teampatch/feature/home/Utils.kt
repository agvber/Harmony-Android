package com.teampatch.feature.home

import android.content.Context
import com.teampatch.feature.R
import java.time.LocalTime

internal fun LocalTime.toStringFormat(context: Context) = with(context) {
    val minute = minute.takeIf { it > 0 }

    when (hour) {
        12 -> minute?.let { getString(R.string.home_text_time_format_pm, 12, it) }
            ?: getString(R.string.home_text_hour_format_pm, 12)

        24 -> minute?.let { getString(R.string.home_text_time_format_am, 12, it) }
            ?: getString(R.string.home_text_hour_format_am, 12)

        in 1..11 -> minute?.let { getString(R.string.home_text_time_format_pm, hour, minute) }
            ?: getString(R.string.home_text_hour_format_pm, hour)

        else -> minute?.let { getString(R.string.home_text_time_format_pm, hour - 12, minute) }
            ?: getString(R.string.home_text_hour_format_pm, hour - 12)
    }
}