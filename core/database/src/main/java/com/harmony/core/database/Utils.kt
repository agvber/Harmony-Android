package com.harmony.core.database

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val LOCAL_DB_DATE_TIME_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

val LOCAL_DB_TIME_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm")

fun getCurrentTimeLocalDBFormat(): String = LocalDateTime.now().format(LOCAL_DB_DATE_TIME_FORMATTER)