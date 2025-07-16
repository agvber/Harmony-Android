package com.teampatch.core.data.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

internal val serverDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

internal fun Date.toServerDateFormat(): String = LocalDateTime.ofInstant(
    toInstant(),
    ZoneId.systemDefault()
)
    .format(serverDateTimeFormatter)