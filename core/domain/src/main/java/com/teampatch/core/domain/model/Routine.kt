package com.teampatch.core.domain.model

import java.time.Period

data class Routine(
    val id: String,
    val name: String,
    val period: Period,
)
