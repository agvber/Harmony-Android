package com.teampatch.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teampatch.feature.R
import java.time.LocalDateTime

@Composable
internal fun LocalDateTime.stringHour(): String = when (hour) {
    0 -> "${stringResource(R.string.am)} 12시"
    !in 0..12 -> "${stringResource(R.string.pm)} ${hour - 12}${stringResource(R.string.hour)}"
    else -> "${stringResource(R.string.am)} ${hour}${stringResource(R.string.hour)}"
}